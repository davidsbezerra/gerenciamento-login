package br.com.ia.david.bsn.gerenciamento.login.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Data
@Table(name = "LOGIN")
public class LoginEntity implements Serializable {

    private static final String SEQUENCE = "LOGIN_SEQ";

    private static final long serialVersionUID = -8236399378645617426L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    @Column(name = "updated_date")
    private OffsetDateTime updatedDate;

    @Column(name = "email")
    private String email;

    @Column(name = "admin")
    private Boolean admin;

}
