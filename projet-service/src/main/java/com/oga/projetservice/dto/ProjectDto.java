package com.oga.projetservice.dto;

import java.util.List;

public class ProjectDto {

    private long id;
    private String title;
    private String description;
    private String dateDebut;
    private String theme;
    private List<UserDto> userDto;
    private List<TacheDto> tahces;

    public ProjectDto(long id, String title, String description, String dateDebut, String theme, List<UserDto> userDto, List<TacheDto> tahces) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.theme = theme;
        this.userDto = userDto;
        this.tahces = tahces;
    }

    public ProjectDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<UserDto> getUserDto() {
        return userDto;
    }

    public void setUserDto(List<UserDto> userDto) {
        this.userDto = userDto;
    }

    public List<TacheDto> getTahces() {
        return tahces;
    }

    public void setTahces(List<TacheDto> tahces) {
        this.tahces = tahces;
    }
}
