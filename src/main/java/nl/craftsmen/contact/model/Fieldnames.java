package nl.craftsmen.contact.model;

public final class Fieldnames {

    private Fieldnames() {
        throw new UnsupportedOperationException("Cannot initialize utility classes");
    }

    public static final String CONTACT_RECORD = "contactRecord";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ADDRESS_1 = "address1";
    public static final String ADDRESS_2 = "address2";
    public static final String ADDRESS_3 = "address3";
    public static final String ZIPCODE = "zipcode";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String IBAN = "iban";
    public static final String SOCIAL_SECURITY_NUMBER = "socialSecurityNumber";
    public static final String DATE_OF_DEATH = "death";
    public static final String DATE_OF_BIRTH = "birth";

    public static String[] getFieldnames() {
        return new String[]{CONTACT_RECORD, FIRST_NAME, LAST_NAME, ADDRESS_1, ADDRESS_2, ADDRESS_3, ZIPCODE, CITY,
                STATE, PHONE, EMAIL, IBAN, SOCIAL_SECURITY_NUMBER, DATE_OF_DEATH, DATE_OF_BIRTH};
    }
}
