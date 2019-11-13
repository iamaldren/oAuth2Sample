package com.aldren.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_datetime")
    private String createdDatetime;

    @Column(name = "updated_datetime")
    private String updatedDatetime;

}
