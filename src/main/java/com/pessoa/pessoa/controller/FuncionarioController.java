package com.pessoa.pessoa.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Comparator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import com.pessoa.pessoa.dto.FuncionarioRecordDTO;
import com.pessoa.pessoa.models.FuncionarioModel;
import com.pessoa.pessoa.repository.FuncionarioRepository;

import jakarta.validation.Valid;


@RestController
public class FuncionarioController {
    
    @Autowired 
    FuncionarioRepository funcionarioRepository;

    // ------------------------ CRUD ----------------------------------
    /*
        INSERE FUNCIONARIO / CABEÇALHO POST
        CHAMADA: localhost:8080/funcionario
        JSON:
        [   {
                "vch_nome": "Maria",
                "sdt_nascimento": "2000-10-18",
                "mny_salario": 2009.04,
                "vch_funcao": "Operador"
            },
            {
                "vch_nome": "João",
                "sdt_nascimento": "1990-05-12",
                "mny_salario": 2284.38,
                "vch_funcao": "Operador"
            },
            {
                "vch_nome": "Caio",
                "sdt_nascimento": "1961-05-02",
                "mny_salario": 9836.14,
                "vch_funcao": "Coordenador"
            },
            {
                "vch_nome": "Miguel",
                "sdt_nascimento": "1988-10-14",
                "mny_salario": 19119.88,
                "vch_funcao": "Diretor"
            },
            {
                "vch_nome": "Alice",
                "sdt_nascimento": "1995-01-05",
                "mny_salario": 2234.68,
                "vch_funcao": "Recepcionista"
            },
            {
                "vch_nome": "Heitor",
                "sdt_nascimento": "1999-11-19",
                "mny_salario": 1582.72,
                "vch_funcao": "Operador"
            },
            {
                "vch_nome": "Artur",
                "sdt_nascimento": "1993-03-31",
                "mny_salario": 4071.84,
                "vch_funcao": "Contador"
            },
            {
                "vch_nome": "Laura",
                "sdt_nascimento": "1994-07-08",
                "mny_salario": 3017.45,
                "vch_funcao": "Gerente"
            },
            {
                "vch_nome": "Heloisa",
                "sdt_nascimento": "2003-05-24",
                "mny_salario": 1606.85,
                "vch_funcao": "Eletricista"
            },
            {
                "vch_nome": "Helena",
                "sdt_nascimento": "1996-09-02",
                "mny_salario": 2799.93,
                "vch_funcao": "Gerente"
            }
        ]
    */
    @PostMapping("/funcionario") 
    public ResponseEntity<List<FuncionarioModel>> insertBatch(@RequestBody List<@Valid FuncionarioRecordDTO> funcionariosDto){
        List<FuncionarioModel> funcionariosModel = new ArrayList<>();

        for (FuncionarioRecordDTO funcionarioDto : funcionariosDto) {
            FuncionarioModel funcionarioModel = new FuncionarioModel();
            BeanUtils.copyProperties(funcionarioDto, funcionarioModel);
            funcionariosModel.add(funcionarioModel);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioRepository.saveAll(funcionariosModel));
    }
    /*
        RETORNA TODOS FUNCIONARIOS / CABEÇALHO GET
        CHAMADA: localhost:8080/funcionario
    */
    @GetMapping("/funcionario")//
    public ResponseEntity<List<FuncionarioModel>> select(){
        List<FuncionarioModel> funcionarioList = funcionarioRepository.findAll();
        // Ordenar a lista por nome
        funcionarioList.sort(Comparator.comparing(FuncionarioModel::getVch_nome));
        if(!funcionarioList.isEmpty()){
            for(FuncionarioModel funcionario : funcionarioList){
                Integer id = funcionario.getCod_funcionario();
                //Adicionar Link para cesso via GET no JSON
                funcionario.add(linkTo(methodOn(FuncionarioController.class).getOneFuncionario(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioList);
    }
    /*
        RETORNA FUNCIONARIO ESPECIFICO PELO ID / CABEÇALHO GET
        CHAMADA: localhost:8080/funcionario/ID
    */
	@GetMapping("/funcionario/{id}")
    public ResponseEntity<Object> getOneFuncionario(@PathVariable(value = "id") Integer id){
        Optional<FuncionarioModel> funcionario = funcionarioRepository.findById(id);
        if(funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionario não encontrado");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(funcionario.get());
        }
    }
    /*
        MODIFICA DADOS FUNCIONARIO / CABEÇALHO PUT
        CHAMADA: localhost:8080/funcionario/1
        JSON EXEMPLO:
            {
                "vch_nome": "João Silva",
                "sdt_nascimento": "1991-01-01",
                "mny_salario": 4000.00,
                "vch_funcao": "Analista"
            }
    */
	@PutMapping("/funcionario/{id}") 
    public ResponseEntity<Object> update(@PathVariable(value = "id") Integer id,
                                                  @RequestBody @Valid FuncionarioRecordDTO funcionarioRecordDto){
        Optional<FuncionarioModel> funcionario = funcionarioRepository.findById(id);
        if(funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionario não encontrado");
        }else{
            var funcionarioModel = funcionario.get();
            // Atualiza os campos da classe pai
            funcionarioModel.setVch_nome(funcionarioRecordDto.getVch_nome());
            funcionarioModel.setSdt_nascimento(funcionarioRecordDto.getSdt_nascimento());
            // Atualiza os campos da classe filha
            funcionarioModel.setMny_salario(funcionarioRecordDto.getMny_salario());
            funcionarioModel.setVch_funcao(funcionarioRecordDto.getVch_funcao());
            return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioRepository.save(funcionarioModel));
        }
    }
    /*
        REMOVE FUNCIONARIO ESPECIFICO PELO ID / CABEÇALHO DELETE
        CHAMADA: localhost:8080/funcionario/ID
    */
	@DeleteMapping("/funcionario/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Integer id){
        Optional<FuncionarioModel> funcionario = funcionarioRepository.findById(id);
        if(funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionario não encontrado");
        }else{
            funcionarioRepository.delete(funcionario.get());
            return ResponseEntity.status(HttpStatus.CREATED).body("Funcionario Removido");
        }
    }
    // ------------------------ FIM CRUD ----------------------------------

    /*
     * 3.2 – Remover o funcionário “João” da lista.
     *  Não há necessidade de criar um metodo delete para remover apenas 1 funcionario, visto que
     * pode-se usar o metodo delete acima, passando o codigo do funcionario João (no caso 2) já 
     * que ele é o 2º registro na ordem e como o codigo é autoincremental ele vincula o funcionario João
     * ao cod_funcionario 2, então segue o caminho abaixo:
     * 
     * REMOVE FUNCIONARIO JOÃO PELO ID / CABEÇALHO DELETE
        CHAMADA: localhost:8080/funcionario/2
     */


    /*
     * 3.3 – Imprimir todos os funcionários com todas suas informações, sendo que:
     * informação de data deve ser exibido no formato dd/mm/aaaa;
     * informação de valor numérico deve ser exibida no formatado com separador de milhar como ponto e decimal como vírgula.
     * 
     * RETORNA FUNCIONARIOS / CABEÇALHO GET
     * CHAMADA: localhost:8080/funcao
     */
    @GetMapping("/funcao")
    public ResponseEntity<Map<String, List<FuncionarioModel>>> obterFuncionariosAgrupadosPorFuncao() {
        List<FuncionarioModel> funcionarios = funcionarioRepository.findAll();

        Map<String, List<FuncionarioModel>> funcionariosAgrupadosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(FuncionarioModel::getVch_funcao));

        return ResponseEntity.ok(funcionariosAgrupadosPorFuncao);
    }

    /* 
     * 3.4 – Os funcionários receberam 10% de aumento de salário, atualizar a lista de funcionários com novo valor.
     * 
     *  ATUALIZA EM 10% O SALARIO DE TODOS OS FUNCIONARIOS / CABEÇALHO PUT
     *  CHAMADA: localhost:8080/funcionario_aumento
     * 
    */
    @PutMapping("/funcionario_aumento")
    public ResponseEntity<List<FuncionarioModel>> aumento(){
        List<FuncionarioModel> funcionarioList = funcionarioRepository.findAll();
        funcionarioList.forEach(funcionario -> {
            BigDecimal salarioAtualizado = funcionario.getMny_salario().multiply(new BigDecimal("1.1"));
            funcionario.setMny_salario(salarioAtualizado);
            var funcionarioRecordDTO = new FuncionarioRecordDTO(funcionario.getCod_funcionario(), funcionario.getVch_nome(), funcionario.getSdt_nascimento(), salarioAtualizado, funcionario.getVch_funcao());
            this.update(funcionario.getCod_funcionario(), funcionarioRecordDTO);
        });
        for(FuncionarioModel funcionario : funcionarioList){
            Integer id = funcionario.getCod_funcionario();
            funcionario.add(linkTo(methodOn(FuncionarioController.class).getOneFuncionario(id)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioList);
    }

    /*
     * 3.5 – Agrupar os funcionários por função em um MAP, sendo a chave a “função” e o valor a “lista de funcionários”.
     * 
     * RETORNA FUNCIONARIOS AGRUPADOS POR FUNCAO / CABEÇALHO GET
     * CHAMADA: localhost:8080/funcao
     */

     /*
     * 3.6 – Imprimir os funcionários, agrupados por função.
     * 
     * RETORNA FUNCIONARIOS AGRUPADOS POR FUNCAO / CABEÇALHO GET
     * CHAMADA: localhost:8080/funcao
     */
    
     /*
     * 3.8 – Imprimir os funcionários que fazem aniversário no mês 10 e 12.
     * 
     * RETORNA FUNCIONARIOS NASCIDOS NO MES 10 e 12 / CABEÇALHO GET
     * CHAMADA: localhost:8080/funcao
     */
    @GetMapping("/aniversario")
    public ResponseEntity<List<FuncionarioModel>> aniversario(){
        List<FuncionarioModel> funcionarioList = new ArrayList<>();
        // Adiciona funcionários do mês 10 à lista
        funcionarioList.addAll(funcionarioRepository.findByMesNascimento(10));
        // Adiciona funcionários do mês 12 à lista
        funcionarioList.addAll(funcionarioRepository.findByMesNascimento(12));
        // Adiciona links para cada funcionário na lista
        for (FuncionarioModel funcionario : funcionarioList) {
            Integer id = funcionario.getCod_funcionario();
            funcionario.add(linkTo(methodOn(FuncionarioController.class).getOneFuncionario(id)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioList);

    }

    /*
     * 3.9 – Imprimir o funcionário com a maior idade, exibir os atributos: nome e idade.
     * 
     * RETORNA FUNCIONARIO MAIS VELHO / CABEÇALHO GET
     * CHAMADA: localhost:8080/idade
     */
    @GetMapping("/idade")
    public ResponseEntity<Object> idade(){
        FuncionarioModel funcionario = funcionarioRepository.findOldestEmployee(PageRequest.of(0, 1)).stream().findFirst().orElse(null);
        if(funcionario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionario não encontrado");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(funcionario);
        }
    }

    /*
     * 3.10 – Imprimir a lista de funcionários por ordem alfabética.
     * Já está implementado no retorno de todos os funcionarios, a orem alfabética
     * 
     * RETORNA FUNCIONARIO EM ORDEM ALFABÉTICA / CABEÇALHO GET
     * CHAMADA: localhost:8080/funcionario
     */


     /*
     * 3.11 – Imprimir o total dos salários dos funcionários.
     * 
     * RETORNA A SOMA DE TODOS OS SALARIOS / CABEÇALHO GET
     * CHAMADA: localhost:8080/salario
     */
    @GetMapping("/salario")
    public ResponseEntity<String> calcularSomaSalarios() {
    List<FuncionarioModel> funcionarioList = funcionarioRepository.findAll();
        // Calcular a soma dos salários
        BigDecimal somaSalarios = funcionarioList.stream()
                .map(FuncionarioModel::getMny_salario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.status(HttpStatus.OK).body("Soma dos salários: " + somaSalarios);
    }

    /*
     * 3.12 – Imprimir quantos salários mínimos ganha cada funcionário, considerando que o salário mínimo é R$1212.00.
     * 
     * RETORNA A SOMA DE TODOS OS SALARIOS / CABEÇALHO GET
     * CHAMADA: localhost:8080/salario
     */
    @GetMapping("/minimo")
    public ResponseEntity<String> calcularSalariosMinimos() {
    List<FuncionarioModel> funcionarioList = funcionarioRepository.findAll();

    // Salário mínimo
    BigDecimal salarioMinimo = new BigDecimal("1212.00");
    String retorno = "";
    for(FuncionarioModel funcionario : funcionarioList){
        retorno += "Nome: "+funcionario.getVch_nome()+" - ";
        retorno += "Salários Mínimos: "+funcionario.getMny_salario().divide(salarioMinimo, 2, RoundingMode.HALF_UP)+"\n";
    }

    return ResponseEntity.status(HttpStatus.OK).body(retorno);
}
}
