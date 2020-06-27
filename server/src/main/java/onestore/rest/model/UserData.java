package onestore.rest.model;

import java.util.Objects;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class UserData {
    private String id;
    private Object content;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public UserData id(String id) {
        this.id = id;
        return this;
    }

    public UserData content(Object content) {
        this.content = content;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserData)) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(id, userData.id) && Objects.equals(content, userData.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }

    public static UserData fromCoreModel(onestore.core.model.UserData userData) {
        return new UserData()
            .id(userData.getId())
            .content(userData.getContent());
    }

    public static onestore.core.model.UserData toCoreModel(UserData userData) {
        return new onestore.core.model.UserData()
            .id(userData.getId())
            .content(userData.getContent());
    }

}