package com.cws.shop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_document_mapping")
public class UserDocumentMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "url_type")
    private String urlType;

    @Column(name = "orignal_url")
    private String orignalUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserDocumentMapping() {
    }

    public UserDocumentMapping(Long id, String url, String orignalUrl, String urlType) {
        this.id = id;
        this.url = url;
        this.orignalUrl = orignalUrl;
        this.urlType = urlType;
    }

    public UserDocumentMapping(Long id, String url, String urlType, String orignalUrl, User user) {
        this.id = id;
        this.url = url;
        this.urlType = urlType;
        this.orignalUrl = orignalUrl;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUrlType() { return urlType; }
    public void setUrlType(String urlType) { this.urlType = urlType; }

    public String getOrignalUrl() { return orignalUrl; }
    public void setOrignalUrl(String orignalUrl) { this.orignalUrl = orignalUrl; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String url;
        private String urlType;
        private String orignalUrl;
        private User user;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder url(String url) { this.url = url; return this; }
        public Builder urlType(String urlType) { this.urlType = urlType; return this; }
        public Builder orignalUrl(String orignalUrl) { this.orignalUrl = orignalUrl; return this; }
        public Builder user(User user) { this.user = user; return this; }

        public UserDocumentMapping build() {
            UserDocumentMapping m = new UserDocumentMapping();
            m.id = this.id;
            m.url = this.url;
            m.urlType = this.urlType;
            m.orignalUrl = this.orignalUrl;
            m.user = this.user;
            return m;
        }
    }
}
