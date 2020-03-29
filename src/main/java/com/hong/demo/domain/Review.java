
package com.hong.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review
{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @NotBlank(message="Please provide a valid name")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message="Please provide a valid email address")
    @Email(message="Please provide a valid email address")
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_on")
    private Date createdOn = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_on")
    private Date updatedOn;
    
    @JsonBackReference
    @ManyToOne(optional=false)
    @JoinColumn(name="book_id")
    private Book book;

}
