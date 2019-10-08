/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tenacle.sgr.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author samuel
 */
@Entity
@Table(name = "payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p")
    , @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.id = :id")
    , @NamedQuery(name = "Payment.findByAmount", query = "SELECT p FROM Payment p WHERE p.amount = :amount")
    , @NamedQuery(name = "Payment.findByTime", query = "SELECT p FROM Payment p WHERE p.time = :time")
    , @NamedQuery(name = "Payment.findByTimeBound", query = "SELECT p FROM Payment p WHERE p.time BETWEEN :start AND :stop")
    , @NamedQuery(name = "Payment.PaymentMethodSummary", query = "SELECT p.paymentMethod.mode as mode, SUM(p.amount) as total FROM Payment p GROUP BY p.id ORDER BY p.id ASC")
    , @NamedQuery(name = "Payment.findByPaymentMethod", query = "SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod")
})
public class Payment implements Serializable, TenacleEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "amount")
    private double amount;
    @Basic(optional = false)
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Size(max = 45)
    @Column(name = "payment_reference")
    private String paymentReference;
    @JoinColumn(name = "payment_method", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PaymentMode paymentMethod;

    public Payment() {
    }

    public Payment(Integer id) {
        this.id = id;
    }

    public Payment(Integer id, double amount, Date time, PaymentMode paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.time = time;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("KES: %s Mode: %s, Date: %s", amount, paymentMethod, time);
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public PaymentMode getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMode paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
