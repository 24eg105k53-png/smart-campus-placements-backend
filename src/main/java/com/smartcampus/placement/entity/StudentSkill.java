package com.smartcampus.placement.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "student_skills")
public class StudentSkill {

    @Embeddable
    public static class StudentSkillId implements Serializable {
        @Column(name = "student_profile_id")
        private Long studentProfileId;

        @Column(name = "skill_id")
        private Long skillId;

        public StudentSkillId() {}

        public StudentSkillId(Long studentProfileId, Long skillId) {
            this.studentProfileId = studentProfileId;
            this.skillId = skillId;
        }

        public Long getStudentProfileId() { return studentProfileId; }
        public void setStudentProfileId(Long studentProfileId) { this.studentProfileId = studentProfileId; }

        public Long getSkillId() { return skillId; }
        public void setSkillId(Long skillId) { this.skillId = skillId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof StudentSkillId that)) return false;
            return Objects.equals(studentProfileId, that.studentProfileId) &&
                   Objects.equals(skillId, that.skillId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentProfileId, skillId);
        }
    }

    @EmbeddedId
    private StudentSkillId id = new StudentSkillId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentProfileId")
    @JoinColumn(name = "student_profile_id")
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "proficiency_level", length = 30)
    private String proficiencyLevel = "INTERMEDIATE";

    public StudentSkill() {}

    public StudentSkill(StudentProfile studentProfile, Skill skill, String proficiencyLevel) {
        this.studentProfile = studentProfile;
        this.skill = skill;
        this.proficiencyLevel = proficiencyLevel != null ? proficiencyLevel : "INTERMEDIATE";
        this.id = new StudentSkillId(studentProfile.getId(), skill.getId());
    }

    public StudentSkillId getId() { return id; }
    public void setId(StudentSkillId id) { this.id = id; }

    public StudentProfile getStudentProfile() { return studentProfile; }
    public void setStudentProfile(StudentProfile studentProfile) { this.studentProfile = studentProfile; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentSkill that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
