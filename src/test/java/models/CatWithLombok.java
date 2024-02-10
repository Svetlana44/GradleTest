package models;

import groovy.transform.ToString;
import lombok.*;

/* @Data может заменить несколько аннотаций  */
/* Lombok plugin автоматом добавляет конструкторы, гетеры/сетеры, toString и пр.  */
@ToString
/* lombok аннотация, добавить конструктор со всеми полями */
@AllArgsConstructor
/* пустой конструктор  */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

/* Помогает создать объект, указывая не все аргументы для конструктора, н-р, если их много
нужно у объекта вызвать билдер
   Cat cat = Cat.builder().isWhite(true).name("Kissy");

   */
@Builder
public class CatWithLombok {
    private String name;
    private String model;
    private Integer age;
    private Boolean isWhite;
}
