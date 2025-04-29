/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Projetjava;

import java.time.LocalDate;

/**
 *
 * @author LENOVO
 */
public class Cart {
    private int id;
    private int guest_id;
    private int user_id;
    private LocalDate created_at;

    public int getId() {
        return id;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Cart{" + "id=" + id + ", guest_id=" + guest_id + ", user_id=" + user_id + ", created_at=" + created_at + '}';
    }

    public Cart(int id, int guest_id, int user_id, LocalDate created_at) {
        this.id = id;
        this.guest_id = guest_id;
        this.user_id = user_id;
        this.created_at = created_at;
    }
    
}
