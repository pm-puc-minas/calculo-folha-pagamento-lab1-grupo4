[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=20320099&assignment_repo_type=AssignmentRepo)

### Herança

A classe `Funcionario` herda da classe `Usuario`:

```java
public class Usuario {
    private String login;
    private String senha;
}

public class Funcionario extends Usuario {
    private String nome;
    private Double salarioBruto;
    // outros atributos do funcionário
}
```

Com isso, `Funcionario` possui automaticamente os atributos `login` e `senha` de `Usuario`, além dos seus próprios atributos.

### Interfaces

A interface `ICalculo` define os métodos que todas as classes de cálculo devem ter:

```java
public interface ICalculo {
    double calcular(double valorBase);
    String getNome();
}
```

### Polimorfismo

Diferentes classes implementam a interface `ICalculo` de formas diferentes:

- `CalculoINSSService` calcula o INSS
- `CalculoIRRFService` calcula o IRRF  
- `CalculoFGTSService` calcula o FGTS

Todas usam o mesmo método `calcular()`, mas cada uma faz o cálculo de um jeito diferente. A classe `CalculoService` usa essa lista de cálculos sem precisar saber qual tipo específico está sendo usado.