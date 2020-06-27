package onestore.rest.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import onestore.core.model.UserRight;

public class UserMetadata {
    private String dataContext;
    private String owner;
    private List<String> connectedUserRights;
    private List<String> anonymousUserRights;

    public String getDataContext() {
        return this.dataContext;
    }

    public void setDataContext(String dataContext) {
        this.dataContext = dataContext;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getConnectedUserRights() {
        return this.connectedUserRights;
    }

    public void setConnectedUserRights(List<String> connectedUserRights) {
        this.connectedUserRights = connectedUserRights;
    }

    public List<String> getAnonymousUserRights() {
        return this.anonymousUserRights;
    }

    public void setAnonymousUserRights(List<String> anonymousUserRights) {
        this.anonymousUserRights = anonymousUserRights;
    }

    public UserMetadata dataContext(String dataContext) {
        this.dataContext = dataContext;
        return this;
    }

    public UserMetadata owner(String owner) {
        this.owner = owner;
        return this;
    }

    public UserMetadata connectedUserRights(List<String> connectedUserRights) {
        this.connectedUserRights = connectedUserRights;
        return this;
    }

    public UserMetadata anonymousUserRights(List<String> anonymousUserRights) {
        this.anonymousUserRights = anonymousUserRights;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserMetadata)) {
            return false;
        }
        UserMetadata userMetadata = (UserMetadata) o;
        return Objects.equals(dataContext, userMetadata.dataContext) && Objects.equals(owner, userMetadata.owner) && Objects.equals(connectedUserRights, userMetadata.connectedUserRights) && Objects.equals(anonymousUserRights, userMetadata.anonymousUserRights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataContext, owner, connectedUserRights, anonymousUserRights);
    }

    @Override
    public String toString() {
        return "{" +
            " dataContext='" + getDataContext() + "'" +
            ", owner='" + getOwner() + "'" +
            ", connectedUserRights='" + getConnectedUserRights() + "'" +
            ", anonymousUserRights='" + getAnonymousUserRights() + "'" +
            "}";
    }

    public static UserMetadata fromCoreModel(onestore.core.model.UserMetadata metadata) {
        return new UserMetadata()
            .dataContext(metadata.getDataContext())
            .owner(metadata.getOwner())
            .connectedUserRights(metadata.getConnectedUserRights().stream().map(r -> r.toString()).collect(Collectors.toList()))
            .anonymousUserRights(metadata.getAnonymousUserRights().stream().map(r -> r.toString()).collect(Collectors.toList()))
        ;
    }

    public static onestore.core.model.UserMetadata toCoreModel(UserMetadata metadata) {
        return new onestore.core.model.UserMetadata()
            .dataContext(metadata.getDataContext())
            .owner(metadata.getOwner())
            .connectedUserRights(metadata.getConnectedUserRights().stream().map(r -> UserRight.valueOf(r)).collect(Collectors.toList()))
            .anonymousUserRights(metadata.getAnonymousUserRights().stream().map(r -> UserRight.valueOf(r)).collect(Collectors.toList()))
        ;
    }
}