package onestore.core.model;

import java.util.List;
import java.util.Objects;

public class UserMetadata {
    private String dataContext;
    private String owner;
    private List<UserRight> connectedUserRights;
    private List<UserRight> anonymousUserRights;

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

    public List<UserRight> getConnectedUserRights() {
        return this.connectedUserRights;
    }

    public void setConnectedUserRights(List<UserRight> connectedUserRights) {
        this.connectedUserRights = connectedUserRights;
    }

    public List<UserRight> getAnonymousUserRights() {
        return this.anonymousUserRights;
    }

    public void setAnonymousUserRights(List<UserRight> anonymousUserRights) {
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

    public UserMetadata connectedUserRights(List<UserRight> connectedUserRights) {
        this.connectedUserRights = connectedUserRights;
        return this;
    }

    public UserMetadata anonymousUserRights(List<UserRight> anonymousUserRights) {
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

}