public class Test {
    public static void main(String[] args) {
        Driver driver = new Driver();

        driver.describe("testing class A", () -> {
                driver.describe("testing method B", () -> {
                        driver.it("should pass this test", () -> {
                                driver.assertTest(true);
                            });
                        driver.it("should fail this test", () -> {
                                driver.assertTest(false);
                            });
                    });
                driver.it("should do something for class A", () -> {
                        driver.assertTest(true);
                    });
                driver.describe("testing method C", () -> {
                        driver.it("should do that", () -> {
                                driver.assertTest(false);
                            });
                        driver.describe("testing something about method C", () -> {
                                driver.it("should pass this test", () -> {
                                        driver.assertTest(true);
                                    });
                                driver.it("should fail this test", () -> {
                                        driver.assertTest(false);
                                    });
                            });
                    });
            });

        driver.describe("testing class D", () -> {
                driver.describe("testing method E", () -> {
                        driver.it("should pass this test", () -> {
                                driver.assertTest(true);
                            });
                        driver.it("should fail this test", () -> {
                                driver.assertTest(false);
                            });
                    });
                driver.it("should do something for class D", () -> {
                        driver.assertTest(true);
                    });
                driver.describe("testing method F", () -> {
                        driver.it("should do that", () -> {
                                driver.assertTest(false);
                            });
                        driver.describe("testing something about method F", () -> {
                                driver.it("should pass this test", () -> {
                                        driver.assertTest(true);
                                    });
                                driver.it("should fail this test", () -> {
                                        driver.assertTest(false);
                                    });
                            });
                    });
            });

        driver.run();
    } /* main */
}
