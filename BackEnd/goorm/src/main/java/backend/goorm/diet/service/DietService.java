package backend.goorm.diet.service;

import backend.goorm.diet.dto.*;
import backend.goorm.diet.entity.Diet;
import backend.goorm.diet.entity.DietMemo;
import backend.goorm.diet.entity.Food;
import backend.goorm.diet.enums.MealTime;
import backend.goorm.diet.repository.DietMemoRepository;
import backend.goorm.diet.repository.DietRepository;
import backend.goorm.diet.repository.FoodRepository;
import backend.goorm.member.model.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DietService {

    private final DietRepository dietRepository;
    private final FoodRepository foodRepository;
    private final DietMemoRepository dietMemoRepository;

    public List<DietResponseDto> getDietByDate(LocalDate date, Member member) {
        List<Diet> diets = dietRepository.findByDietDateAndMember(date, member);
        String memoContent = getDietMemo(member, date);
        return DietResponseDto.fromEntityListWithMemo(diets, memoContent);
    }

    public List<DietResponseDto> getAllDiets(Member member) {
        List<Diet> diets = dietRepository.findByMember(member);

        // Diet 별로 메모 조회 (N+1 위험, 대량 데이터시 fetch join 등으로 개선 가능)
        return diets.stream()
                .map(diet -> {
                    String memoContent = getDietMemo(member, diet.getDietDate());
                    return DietResponseDto.fromEntityWithMemo(diet, memoContent);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DietResponseDto> createDiet(DietCreateRequestDto dto, Member member) {
        List<DietResponseDto> responses = dto.getFoodQuantities().stream().map(foodQuantity -> {
            Food food = foodRepository.findById(foodQuantity.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodQuantity.getFoodId()));

            Diet diet = Diet.builder()
                    .food(food)
                    .quantity(foodQuantity.getQuantity())
                    .gram(foodQuantity.getGram())
                    .mealTime(MealTime.valueOf(dto.getMealTime().toUpperCase()))
                    .dietDate(dto.getDietDate())
                    .member(member)
                    .build();

            diet.calculateTotalCaloriesAndGram();

            Diet savedDiet = dietRepository.save(diet);

            return DietResponseDto.fromEntity(savedDiet);
        }).collect(Collectors.toList());

        return responses;
    }

    @Transactional
    public List<DietResponseDto> editDietsAndMemos(List<DietUpdateRequestDto> requests, Member member) {
        List<DietResponseDto> updatedDiets = new ArrayList<>();
        LocalDate memoDate = null;
        String memoContent = null;

        for (DietUpdateRequestDto request : requests) {
            Diet diet = dietRepository.findById(request.getDietId())
                    .orElseThrow(() -> new IllegalArgumentException("Diet not found with id: " + request.getDietId()));

            if (!diet.getMember().getMemberId().equals(member.getMemberId())) {
                throw new IllegalArgumentException("You do not have permission to edit this diet.");
            }

            updateDietEntity(diet, request);

            Diet saved = dietRepository.save(diet);

            if (memoDate == null) {
                memoDate = request.getDietDate();
            }
            if (memoContent == null && request.getMemo() != null) {
                memoContent = request.getMemo();
            }
        }

        if (memoDate != null && memoContent != null) {
            Optional<DietMemo> existingMemoOpt = dietMemoRepository.findByMemberAndDate(member, memoDate);

            if (existingMemoOpt.isPresent()) {
                DietMemo existingMemo = existingMemoOpt.get();
                existingMemo.setContent(memoContent);
                dietMemoRepository.save(existingMemo);
            } else {
                DietMemo newMemo = DietMemo.builder()
                        .member(member)
                        .content(memoContent)
                        .date(memoDate)
                        .build();
                dietMemoRepository.save(newMemo);
            }
        }

        // 응답 생성 (변경된 Diet 리스트 변환)
        for (DietUpdateRequestDto request : requests) {
            Diet diet = dietRepository.findById(request.getDietId())
                    .orElseThrow(() -> new IllegalArgumentException("Diet not found with id: " + request.getDietId()));

            String memoContentForDto = getDietMemo(member, diet.getDietDate());

            updatedDiets.add(DietResponseDto.fromEntity(diet, memoContentForDto));
        }

        return updatedDiets;
    }

    // 엔티티 업데이트 로직(서비스 계층에서 담당)
    private void updateDietEntity(Diet diet, DietUpdateRequestDto dto) {
        diet.setDietDate(dto.getDietDate());
        diet.setMealTime(MealTime.valueOf(dto.getMealTime().toUpperCase()));

        if (dto.getFoodQuantities() != null && !dto.getFoodQuantities().isEmpty()) {
            DietUpdateRequestDto.FoodQuantity fq = dto.getFoodQuantities().get(0);
            Food food = foodRepository.findById(fq.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + fq.getFoodId()));
            diet.setFood(food);
            diet.setQuantity(fq.getQuantity());
            diet.setGram(fq.getGram());
        }
        // memo 등 필요한 추가 변경도 여기서 처리
    }

    @Transactional
    public boolean deleteDiet(Long dietId, Member member) {
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Diet not found with id: " + dietId));

        if (!diet.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("You do not have permission to delete this diet.");
        }

        dietRepository.delete(diet);
        return true;
    }

    @Transactional
    public DietMemoDto addOrUpdateDietMemo(DietMemoDto memoDto, Member member) {
        Optional<DietMemo> existingMemoOpt = dietMemoRepository.findByMemberAndDate(member, memoDto.getDate());

        DietMemo memo;
        if (existingMemoOpt.isPresent()) {
            memo = existingMemoOpt.get();
            memo.setContent(memoDto.getContent());
        } else {
            memo = DietMemo.builder()
                    .member(member)
                    .date(memoDto.getDate())
                    .content(memoDto.getContent())
                    .build();
        }

        DietMemo savedMemo = dietMemoRepository.save(memo);
        return DietMemoDto.fromEntity(savedMemo);
    }

    public String getDietMemo(Member member, LocalDate date) {
        return dietMemoRepository.findByMemberAndDate(member, date)
                .map(DietMemo::getContent)
                .orElse(null);
    }

    public Map<String, NutrientPercentage> getNutrientPercentageForDate(Member member, LocalDate date) {
        Map<String, NutrientPercentage> macroPercentages = new HashMap<>();
        double totalCaloriesForDay = 0.0;

        double totalCarbs = 0.0;
        double totalProtein = 0.0;
        double totalFat = 0.0;

        List<Diet> diets = dietRepository.findByDietDateAndMember(date, member);

        for (Diet diet : diets) {
            double carbs = Optional.ofNullable(diet.getFood().getCarbohydrate()).orElse(0.0f);
            double protein = Optional.ofNullable(diet.getFood().getProtein()).orElse(0.0f);
            double fat = Optional.ofNullable(diet.getFood().getFat()).orElse(0.0f);
            double calories = Optional.ofNullable(diet.getTotalCalories()).orElse(0.0f);

            totalCarbs += carbs;
            totalProtein += protein;
            totalFat += fat;
            totalCaloriesForDay += calories;

            NutrientPercentage nutrientPercentage = macroPercentages.computeIfAbsent(diet.getMealTime().toString(), k -> NutrientPercentage.builder().build());

            nutrientPercentage.setCarbsPercentage(nutrientPercentage.getCarbsPercentage() + carbs);
            nutrientPercentage.setProteinPercentage(nutrientPercentage.getProteinPercentage() + protein);
            nutrientPercentage.setFatPercentage(nutrientPercentage.getFatPercentage() + fat);
            nutrientPercentage.setTotalCalories(nutrientPercentage.getTotalCalories() + calories);
        }

        macroPercentages.forEach((mealTime, nutrient) -> {
            double totalNutrients = nutrient.getCarbsPercentage() + nutrient.getProteinPercentage() + nutrient.getFatPercentage();
            if (totalNutrients > 0) {
                nutrient.setCarbsPercentage((int) Math.round((nutrient.getCarbsPercentage() / totalNutrients) * 100));
                nutrient.setProteinPercentage((int) Math.round((nutrient.getProteinPercentage() / totalNutrients) * 100));
                nutrient.setFatPercentage((int) Math.round((nutrient.getFatPercentage() / totalNutrients) * 100));
            } else {
                nutrient.setCarbsPercentage(0);
                nutrient.setProteinPercentage(0);
                nutrient.setFatPercentage(0);
            }
        });

        double totalNutrients = totalCarbs + totalProtein + totalFat;

        NutrientPercentage totalNutrientPercentage = NutrientPercentage.builder().build();
        if (totalNutrients > 0) {
            totalNutrientPercentage.setCarbsPercentage((int) Math.round((totalCarbs / totalNutrients) * 100));
            totalNutrientPercentage.setProteinPercentage((int) Math.round((totalProtein / totalNutrients) * 100));
            totalNutrientPercentage.setFatPercentage((int) Math.round((totalFat / totalNutrients) * 100));
        } else {
            totalNutrientPercentage.setCarbsPercentage(0);
            totalNutrientPercentage.setProteinPercentage(0);
            totalNutrientPercentage.setFatPercentage(0);
        }
        totalNutrientPercentage.setTotalCalories(totalCaloriesForDay);

        macroPercentages.put("TOTAL", totalNutrientPercentage);

        return macroPercentages;
    }
}
