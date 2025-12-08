# Factory Pattern - Sistema de Cálculo de Impostos

## 📋 Visão Geral

O sistema de cálculo de impostos foi refatorado para utilizar o **Factory Pattern**, um padrão de projeto criacional que fornece uma interface para criar objetos sem especificar suas classes concretas.

## 🎯 Benefícios da Implementação

### Antes (sem Factory Pattern)
```java
@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final CalculoINSSService calculoINSS;
    private final CalculoIRRFService calculoIRRF;
    private final CalculoFGTSService calculoFGTS;
    // Para cada novo imposto, adicionar nova dependência
}
```

**Problemas:**
- Alto acoplamento
- Difícil adicionar novos impostos
- Classes de serviço precisam conhecer todos os calculadores
- Viola o princípio Open/Closed (SOLID)

### Depois (com Factory Pattern)
```java
@Service
@RequiredArgsConstructor
public class FuncionarioService {
    private final ImpostoCalculadorFactory impostoFactory;
    // Único ponto de acesso a todos os calculadores
}
```

**Vantagens:**
✅ Baixo acoplamento  
✅ Fácil adicionar novos impostos  
✅ Centralização da lógica de criação  
✅ Segue princípios SOLID  
✅ Código mais testável  
✅ Manutenção simplificada  

## 📁 Estrutura de Arquivos

```
calculo/
├── factory/
│   ├── TipoImposto.java              # Enum com tipos de impostos
│   └── ImpostoCalculadorFactory.java  # Factory principal
├── interfaces/
│   └── ICalculo.java                  # Interface comum
├── CalculoINSSService.java            # Implementação INSS
├── CalculoIRRFService.java            # Implementação IRRF
├── CalculoFGTSService.java            # Implementação FGTS
└── CalculoContribuicaoSindicalService.java # Exemplo de novo imposto
```

## 🔧 Como Usar

### Calculando um Imposto Específico

```java
@Service
@RequiredArgsConstructor
public class ExemploService {
    private final ImpostoCalculadorFactory impostoFactory;

    public void calcular() {
        CalculoContext context = CalculoContext.builder()
            .salarioBruto(5000.00)
            .numeroDependentes(2)
            .build();

        // Obter calculador via Factory
        ICalculo calculadorINSS = impostoFactory.obterCalculador(TipoImposto.INSS);
        double inss = calculadorINSS.calcular(context);

        // Ou de forma mais direta
        double irrf = impostoFactory.obterCalculador(TipoImposto.IRRF)
            .calcular(context);
    }
}
```

### Obtendo Calculador por String

```java
String codigoImposto = "INSS"; // Pode vir de API, banco de dados, etc.
ICalculo calculador = impostoFactory.obterCalculadorPorCodigo(codigoImposto);
double valor = calculador.calcular(context);
```

### Verificando Disponibilidade

```java
if (impostoFactory.isCalculadorDisponivel(TipoImposto.INSS)) {
    // Calculador disponível
}

// Listar todos os tipos disponíveis
TipoImposto[] tipos = impostoFactory.getTiposDisponiveis();
```

## ➕ Adicionando um Novo Imposto

Adicionar um novo imposto requer apenas **3 passos simples**:

### 1️⃣ Criar a Classe de Cálculo

```java
@Service
public class CalculoPISService implements ICalculo {
    
    @Override
    public double calcular(CalculoContext context) {
        // Lógica de cálculo do PIS (0.65% do salário)
        return context.getSalarioBruto() * 0.0065;
    }

    @Override
    public String getTipo() {
        return "PIS";
    }
}
```

### 2️⃣ Adicionar no Enum TipoImposto

```java
public enum TipoImposto {
    INSS("INSS", "Instituto Nacional do Seguro Social"),
    IRRF("IRRF", "Imposto de Renda Retido na Fonte"),
    FGTS("FGTS", "Fundo de Garantia do Tempo de Serviço"),
    PIS("PIS", "Programa de Integração Social"); // ← Novo imposto
    
    // ... resto do código
}
```

### 3️⃣ Registrar na Factory

```java
@Component
public class ImpostoCalculadorFactory {
    
    private void registrarCalculadores() {
        calculadores.put(TipoImposto.INSS, new CalculoINSSService());
        calculadores.put(TipoImposto.IRRF, new CalculoIRRFService());
        calculadores.put(TipoImposto.FGTS, new CalculoFGTSService());
        calculadores.put(TipoImposto.PIS, new CalculoPISService()); // ← Registrar
    }
}
```

**Pronto!** O novo imposto está disponível em todo o sistema. ✨

## 🏗️ Arquitetura do Padrão

```
┌─────────────────────┐
│  TipoImposto (Enum) │
│  - INSS             │
│  - IRRF             │
│  - FGTS             │
└──────────┬──────────┘
           │
           │ usa
           ▼
┌──────────────────────────────┐
│ ImpostoCalculadorFactory     │
│                              │
│ + obterCalculador()          │
│ + obterCalculadorPorCodigo() │
│ + isCalculadorDisponivel()   │
└──────────┬───────────────────┘
           │
           │ retorna
           ▼
┌──────────────────────┐
│   ICalculo           │◄───────────┐
│                      │            │
│ + calcular()         │            │
│ + getTipo()          │            │
└──────────────────────┘            │
           △                        │
           │ implementa             │
           │                        │
    ┌──────┴──────────┬─────────┐  │
    │                 │         │  │
┌───┴────┐    ┌──────┴──┐  ┌──┴──┴──┐
│  INSS  │    │  IRRF   │  │  FGTS   │
└────────┘    └─────────┘  └─────────┘
```

## 🧪 Testes

### Testando a Factory

```java
@Test
void deveObterCalculadorINSS() {
    ImpostoCalculadorFactory factory = new ImpostoCalculadorFactory();
    ICalculo calculador = factory.obterCalculador(TipoImposto.INSS);
    
    assertNotNull(calculador);
    assertEquals("INSS", calculador.getTipo());
}

@Test
void deveLancarExcecaoParaTipoInvalido() {
    ImpostoCalculadorFactory factory = new ImpostoCalculadorFactory();
    
    assertThrows(IllegalArgumentException.class, () -> {
        factory.obterCalculadorPorCodigo("INVALIDO");
    });
}
```

### Testando um Calculador

```java
@Test
void deveCalcularINSSCorretamente() {
    CalculoINSSService calculador = new CalculoINSSService();
    CalculoContext context = CalculoContext.builder()
        .salarioBruto(5000.00)
        .build();
    
    double resultado = calculador.calcular(context);
    
    assertTrue(resultado > 0);
    assertTrue(resultado < 5000.00);
}
```

## 📊 Comparação de Complexidade

### Adicionar Novo Imposto

| Aspecto | Sem Factory | Com Factory |
|---------|-------------|-------------|
| Arquivos modificados | 4-6 | 3 |
| Linhas de código | ~50 | ~30 |
| Pontos de injeção | Múltiplos | 1 |
| Tempo estimado | 30-45 min | 10-15 min |
| Risco de quebrar código | Alto | Baixo |

## 🎓 Conceitos SOLID Aplicados

1. **Single Responsibility Principle (SRP)**
   - Cada calculador tem uma única responsabilidade
   - Factory responsável apenas pela criação

2. **Open/Closed Principle (OCP)**
   - Sistema aberto para extensão (novos impostos)
   - Fechado para modificação (não altera código existente)

3. **Liskov Substitution Principle (LSP)**
   - Todos os calculadores são substituíveis via interface ICalculo

4. **Dependency Inversion Principle (DIP)**
   - Classes dependem da abstração (ICalculo), não de implementações concretas

## 📖 Referências

- **Design Patterns: Elements of Reusable Object-Oriented Software** - Gang of Four
- **Clean Code** - Robert C. Martin
- **Refactoring: Improving the Design of Existing Code** - Martin Fowler

## 🚀 Próximos Passos

Possíveis melhorias futuras:

1. **Cache de Calculadores**: Reutilizar instâncias já criadas
2. **Calculadores Customizáveis**: Permitir configuração via banco de dados
3. **Strategy Pattern**: Combinar com Strategy para cálculos mais complexos
4. **Async Processing**: Cálculos assíncronos para folhas grandes
5. **Audit Trail**: Registrar histórico de cálculos realizados

---

**Desenvolvido com 💙 usando boas práticas de engenharia de software**
