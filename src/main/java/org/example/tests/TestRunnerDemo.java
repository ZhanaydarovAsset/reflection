package org.example.tests;

public class TestRunnerDemo {

  // private никому не видно
  // default (package-private) внутри пакета
  // protected внутри пакета + наследники
  // public всем

  public static void main(String[] args) {
    TestRunner.run(TestRunnerDemo.class);
  }
  @BeforeAll
  void printFront(){
    System.out.println("BeforeAll");
  }

  @BeforeEach
  void printEach(){
    System.out.println("BeforeEach");
  }

  @AfterAll
  void printAfterAll(){
    System.out.println("After All");
  }

  @AfterEach
  void printAfterEach(){
    System.out.println("After Each");
  }

  @Test(order = 3)
  void test1() {
    System.out.println("test1");
  }

  @Test(order = 1)
  private void test2() {
    System.out.println("test2");
  }

 @Test
  void test3() {
    System.out.println("test3");
  }

}
