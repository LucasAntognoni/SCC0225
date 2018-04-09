package com.exemple.android.crud_exercicio;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO 42: Faça Contact implementar a interface Parcelable. Use o próprio android Studio para criar e implementar os métodos necessários.
 */
public class Contact implements Parcelable{

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    /**
     * TODO 43: Lembre-se de implementar um construtor que não recebe parâmetros.
     */

    protected Contact(Parcel in) {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        phone = in.readString();
    }

    public Contact() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected int id;
    protected String first_name, last_name, email, phone;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(phone);
    }
}
