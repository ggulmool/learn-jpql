package me.ggulmool.entity.dto;

public class UserDTO {

    private String username;
    private Integer age;

    public UserDTO(String username, Integer age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("UserDTO[%s, %d]", username, age);
    }
}
