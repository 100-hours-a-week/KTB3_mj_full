package com.example.restapi_demo.user.model;

public class User {
    private Long id;//고유ID
    private String email;//이메일 로그인ID
    private String password;//비밀번호
    private String nickname;//닉네임
    private String profileImage;//프사

    //생성자 선언
    public User(Long id, String email, String password, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    //getter
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNickname() { return nickname; }
    public String getProfileImage() { return profileImage; }

    // setter
    public void setPassword(String password) { this.password = password; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
