import com.github.javafaker.Faker;

class FakerUtils {
    private static final Faker faker = new Faker();

    static String getRandomProductName() {
        return faker.commerce().productName();
    }

    static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    static String getRandomName() {
        return faker.name().fullName();
    }
}
