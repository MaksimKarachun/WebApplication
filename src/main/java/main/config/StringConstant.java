package main.config;

public class StringConstant {

  public static final String VALIDATION_ERROR_MESSAGE = "Неверный формат запроса.";

  public static final String CAPTCHA_CODE_ERROR_MESSAGE = "Код с картинки введен неверно.";

  public static final String USER_ALREADY_REGISTER_ERROR_MESSAGE = "Данный пользователь уже зарегистрирован.";

  public static final String USER_NAME_ERROR_MESSAGE = "Недопустимое имя пользователя.";

  public static final String DATA_BASE_ERROR_MESSAGE = "Ошибка при обращении к БД.";

  public static final String NOT_ALLOWED_IMAGE_TYPE_ERROR_MESSAGE = "Недопустимый формат изображения.";

  public static final String POST_NOT_FOUND = "Пост не найден.";

  public static final String USER_NOT_FOUND = "Пользователь не найден.";

  public static final String USER_WITH_EMAIL_ALREADY_EXIST = "Пользователь с указанным email уже зарегистрирован.";

  public static final String PASSWORD_LENGTH_ERROR = "Пароль короче 6-ти символов";

  public static final String RECOVERY_PASSWORD_LINK_MESSAGE = "Ссылка для восстановления пароля устарела <a href=\"/auth/restore\">Запросить ссылку снова</a>";

  private StringConstant() {
  }
}
