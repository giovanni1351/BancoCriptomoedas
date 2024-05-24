Português:
O projeto Exchange Criptomoedas é um projeto de um sistema bancário direcionado a criptomoedas desenvolvido na linguagem Java, com integração em banco de dados em SQL por meio do sistema PostgreSQL e do software pgAdmin 4. O banco trabalha com uma interface gráfica com usuário, feita seguindo a arquitetura MVC, na qual o mesmo será apresentado a todas as funções
do programa após realizar cadastro ou fazer login (caso já tenha cadastrado um usuário), além de possuir um sistema de dois diferentes tipos de usuário, o cliente normal e o administrador. As funções disponíveis a cada um são:
Para clientes normais: Efetuar login/cadastro, Consultar saldo (em reais e em todas as criptomoedas disponíveis) da carteira de investimentos, consultar seu extrato de operações da carteira de investimentos, depositar valores (em reais) na carteira de investimentos, sacar valores (em reais) da carteira de investimento. comprar e vender criptomoedas e atualizar
a cotação das criptomoedas. O programa, por padrão, já possui três criptomoedas cadastradas (Bitcoin, Ethereum e Ripple), as quais o usuário pode comprar utilizando o saldo que possui em reais, também podendo vendê-las por um valor em reais.
Para o administrador: Efetuar login, cadastrar novos investidores, excluir investidores, cadastrar e excluir novas criptomoedas, listar todos os investidores cadastrados, consultar dados de todos os investidores (que incluem extrato e saldo) e atualizar a cotação das criptomoedas.
Todas as ações realizadas no programa são atualizadas juntamente ao banco de dados. Nos arquivos disponíveis no Github, está incluso um script com os dados do banco de dados de teste (que já possui uma série de informações cadastradas) e um vazio, caso o usuário deseje iniciar o seu do zero. Os scripts são executados pelo pgAdmin 4, sendo necessário possuir
o PostgreSQL instalado.
Para executar o programa, basta realizar o download do executável .jar e executá-lo, lembrando que, para o funcionamento próprio do programa, é necessário possuir o Java, o PostgreSQL e o pgAdmin 4 (com o banco de dados construído).
Caso deseje compilar o código por meio de alguma IDE ou terminal, as mesmas condições se aplicam.

No desenvolvimento do código, foram utilizados 5 packages.
O package DAO, que possui duas classes: Conexao, realiza a conexão do usuário com a database e PessoaDAO que realiza a consulta e manipulação de dados na database. 
O package view, que possui os JFrames das três interfaces utilizadas no programa.
O package model, que possui 13 classes responsáveis por realizar as operações desejadas pelo usuário e retornar seus valores para o Controller.
O package controller, que gerencia o fluxo de informações entre view e model.
E o package exchangecripto que possui a classe main do programa.
Também foi utilizada a biblioteca postgresql-42.7.3.jar, necessária para realizar a conexão do usuário com o banco de dados.

English:
The Exchange Criptomoedas is a project that consists in a cryptocurrency based bank system developed in Java, integrated with an SQL database through the PostgreSQL system and the software pgAdmin4. The bank works with a GUI, that follows the MVC architeture, in which the user is presented with all the functions of the program once he registers an account
or logs in (in case he has already registered an account). The bank also works with two different types of users, the normal client and the admin. The functions available for each are:
For the normal clients: Log in/Register Account, Check bank balance (in reais (brazilian currency) and in all cryptocurrencies available) of your investment wallet, check the bank statement of your investment wallet, make deposits (in reais) to your investment wallet, draw money (also from your balance in reais) from your investment wallet, buy and sell 
cryptocurrencies and update the price of the cryptocurrencies. The program already comes with three cryptocurrencies registered (Bitcoin, Ethereum and Ripple), which the user is able to buy with the money from his current balance in reais and sell for an amount also in reais.
For the admin: Log in, register new investors, delete investors, register and delete new cryptocurrencies, list all registered investors, check the data of all investors (which includes their bank statement and current balance), and manually update the quotation of the cryptocurrencies.
All the actions you make in the program are updated alongside the database. In the files available on the Github page, we included a script with the data from our test database (which already has a bunch of information registered) and an empty one, if you wish to start yours from scratch. The scripts are to be executed through pgAdmin 4, and it is necessary
to have the PostgreSQL installed.
To run the program, just download the executable .jar file and run it. Reminder: For the proper functioning of the program, it is necessary to have Java, PostgreSQL and pgAdmin 4 (with the database built) installed.
If you wish to compile the code through an IDE or your terminal, the same conditions apply.

5 packages were used to develop the code.
The DAO package, which includes two classes: Conexao, which makes the connection between the user and the database, and PessoaDAO, which checks and manipulates the data from the database.
The view package, which includes the JFrames of the three GUIs used in the program.
The model package, which has 13 classes that are responsible for realizing the operations demanded by the user and return their values to the Controller.
The controller package, which manages the information flow between view and model.
And the exchangecripto package which holds the program's main class.
We also used the library postgresql-42.7.3.jar, which is necessary to successfuly make the connection between the user and the database.


