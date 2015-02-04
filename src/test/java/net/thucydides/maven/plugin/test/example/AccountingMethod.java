package net.thucydides.maven.plugin.test.example;

public enum AccountingMethod {

    BALANCE_FORWARD("balanceForward"),
    OPEN_ITEM("openItem");
    private final String value;

    AccountingMethod(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccountingMethod fromValue(String v) {
        for (AccountingMethod c : AccountingMethod.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}