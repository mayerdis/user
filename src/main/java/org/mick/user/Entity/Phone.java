package org.mick.user.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(generator = "uuidPhones")
    @GenericGenerator(name = "uuidPhones", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User userId;

    @Column(name = "number", length = 25)
    @ApiModelProperty(notes = "Numero Telefonico del usuario",name="number",required=true,value="1234-9876", position = 3)
    private String number;

    @Column(name = "citycode", length = 25)
    @ApiModelProperty(notes = "Numero codigo de la ciudad",name="citycode",required=true,value="122", position = 2)
    private String citycode;

    @Column(name = "contrycode", length = 10)
    @ApiModelProperty(notes = "Numero codigo del pais",name="contrycode",required=true,value="505", position = 1)
    private String contrycode;

    public Phone() {
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", citycode='" + citycode + '\'' +
                ", contrycode='" + contrycode + '\'' +
                '}';
    }

}
