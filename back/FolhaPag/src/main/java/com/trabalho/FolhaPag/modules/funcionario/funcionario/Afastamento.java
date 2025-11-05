package com.trabalho.FolhaPag.modules.funcionario.funcionario;


//Afastamento tem um relação de N -> 1, então quem vai ser o "dono" da relação é o afastamento
//e quando formos trabalhar com banco de dados so add @ManyToOne e @OneToMany entre os dois
public class Afastamento {

    private String motivo;
    private Integer diasDeAfastamento;
    private boolean possuiAtestado;

}
