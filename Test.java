import src.GreenTeaLatte;

public class Test {
    public static void main(String[] args) {
        GreenTeaLatte deliciousLatte = new GreenTeaLatte();

        deliciousLatte.setIndentationToSpaces(4);

        deliciousLatte.describe("testing class A", () -> {
                deliciousLatte.describe("testing method B", () -> {
                        deliciousLatte.it("should pass this test", () -> {
                                deliciousLatte.assertTest(true);
                            });
                        deliciousLatte.it("should fail this test", () -> {
                                deliciousLatte.assertTest(false);
                            });
                    });
                deliciousLatte.it("should do something for class A", () -> {
                        deliciousLatte.assertTest(true);
                    });
                deliciousLatte.describe("testing method C", () -> {
                        deliciousLatte.it("should do that", () -> {
                                deliciousLatte.assertTest(false);
                            });
                        deliciousLatte.describe("testing something about method C", () -> {
                                deliciousLatte.it("should pass this test", () -> {
                                        deliciousLatte.assertTest(true);
                                    });
                                deliciousLatte.it("should fail this test", () -> {
                                        deliciousLatte.assertTest(false);
                                    });
                                deliciousLatte.it("pendingTest");
                            });
                    });
            });

        deliciousLatte.describe("testing class D", () -> {
                deliciousLatte.describe("testing method E", () -> {
                        deliciousLatte.it("should pass this test", () -> {
                                deliciousLatte.assertTest(true);
                            });
                        deliciousLatte.it("should fail this test", () -> {
                                deliciousLatte.assertTest(false);
                            });
                        deliciousLatte.it("pendingTest", () -> {});
                    });
                deliciousLatte.it("should do something for class D", () -> {
                        deliciousLatte.assertTest(true);
                    });
                deliciousLatte.describe("testing method F", () -> {
                        deliciousLatte.it("should do that", () -> {
                                deliciousLatte.assertTest(false);
                            });
                        deliciousLatte.describe("testing something about method F", () -> {
                                deliciousLatte.it("should pass this test", () -> {
                                        deliciousLatte.assertTest(true);
                                    });
                                deliciousLatte.it("should fail this test", () -> {
                                        deliciousLatte.assertTest(false);
                                    });
                            });
                    });
            });

        deliciousLatte.run();
    } /* main */
}