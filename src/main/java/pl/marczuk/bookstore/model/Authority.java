package pl.marczuk.bookstore.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    private String username;

    @Column(name = "authority")
    private String authority;

    @ManyToOne
    @JoinColumn(name = "username", updatable = false, insertable = false)
    private User user;

    public Authority(String authority, String username) {
        this.authority = authority;
        this.username = username;
    }

    public Authority() {

    }
}
