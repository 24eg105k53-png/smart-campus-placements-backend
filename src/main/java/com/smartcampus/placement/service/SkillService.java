package com.smartcampus.placement.service;

import com.smartcampus.placement.entity.Skill;
import java.util.List;

public interface SkillService {
    List<Skill> getAllSkills();
    Skill getOrCreateSkill(String name);
}
