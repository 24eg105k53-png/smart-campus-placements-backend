package com.smartcampus.placement.service.impl;

import com.smartcampus.placement.entity.Skill;
import com.smartcampus.placement.repository.SkillRepository;
import com.smartcampus.placement.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    @Transactional
    public Skill getOrCreateSkill(String name) {
        String normalized = name.toLowerCase().trim();
        return skillRepository.findByNormalizedName(normalized)
            .orElseGet(() -> skillRepository.save(new Skill(name.trim(), normalized, "TECHNICAL")));
    }
}
