package ru.yandex.practicum.filmorate.repository;

public class BeanPropertySqlParameterSource extends org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource {
    public BeanPropertySqlParameterSource(Object object) {
        super(object);
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object value = super.getValue(paramName);

        if (value instanceof Enum) {
            return value.toString();
        }

        return value;
    }
}
