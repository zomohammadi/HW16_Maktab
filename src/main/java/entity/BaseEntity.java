package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {

    public static final String ID = "id";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID)
    private Long id;


    @Column(name = CREATE_DATE)
    private ZonedDateTime createDate;


    @Column(name = LAST_UPDATE_DATE)
    private ZonedDateTime lastUpdateDate;


    @PrePersist
    public void perPersist() {
        setCreateDate(ZonedDateTime.now());
        setLastUpdateDate(ZonedDateTime.now());

    }
    @PreUpdate
    public void perUpdate(){
        setLastUpdateDate(ZonedDateTime.now());
    }

}
