package backend.goorm.diet.service;

import backend.goorm.diet.dto.FoodResponseDto;
import backend.goorm.diet.dto.FoodUpdateRequestDto;
import backend.goorm.diet.dto.FoodUserDto;
import backend.goorm.diet.entity.Food;
import backend.goorm.diet.repository.DietRepository;
import backend.goorm.diet.repository.FoodRepository;
import backend.goorm.member.model.entity.Member;
import backend.goorm.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;
    private final DietRepository dietRepository;

    public List<FoodResponseDto> getFoodByName(Long memberId, String foodName) {
        if (foodName == null) {
            return null;
        }


        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        log.info("memberId: {}", member);
        log.info("foodName: {}", foodName);
        Pageable limit = PageRequest.of(0, 100);
        List<Food> foods = foodRepository.findDistinctFoodNameByMember(foodName, member, limit);
        return FoodResponseDto.fromEntityList(foods);
    }

    public List<FoodResponseDto> getAllFoods(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Food> foods = foodRepository.findAll(pageable).getContent();
        return FoodResponseDto.fromEntityList(foods);
    }

    public List<FoodResponseDto> getRecentFood(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        Pageable pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt");
        List<Food> foods = dietRepository.findDistinctFoodByMember(member, pageRequest);
        return FoodResponseDto.fromEntityList(foods);
    }

    public FoodResponseDto createFood(Long memberId, FoodUserDto dto) {


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        Food food = dto.toEntity();
        food.setMember(member);
        foodRepository.save(food);
        return FoodResponseDto.fromEntity(food);
    }

    public void updateFoodEntity(Food food, FoodUpdateRequestDto dto) {
        food.setFoodName(dto.getFoodName());
        food.setCalories(dto.getCalories());
        food.setCarbohydrate(dto.getCarbohydrate());
        food.setProtein(dto.getProtein());
        food.setFat(dto.getFat());
        food.setSugar(dto.getSugar());
        food.setSalt(dto.getSalt());
        food.setCholesterol(dto.getCholesterol());
        food.setSaturatedFat(dto.getSaturatedFat());
        food.setTransFat(dto.getTransFat());
    }


    public FoodResponseDto updateFood(Long memberId, Long foodId, FoodUpdateRequestDto dto) {

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));

        if (!food.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("You do not have permission to update this food item.");
        }

        dto.updateEntity(food);
        foodRepository.save(food);
        return FoodResponseDto.fromEntity(food);
    }

    public boolean deleteFood(Long memberId, Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + foodId));

        if (!food.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("You do not have permission to delete this food item.");
        }

        foodRepository.delete(food);
        return true;
    }
}
