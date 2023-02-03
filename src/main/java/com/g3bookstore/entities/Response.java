package com.g3bookstore.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity class for Responses.
 * 
 * @author Alessandro, Denis, Kevin, Werner
 */
@Entity
@Table(name = "response", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Response.findAll", query = "SELECT r FROM Response r")
    , @NamedQuery(name = "Response.findByResponseId", query = "SELECT r FROM Response r WHERE r.responseId = :responseId")
    , @NamedQuery(name = "Response.findByTotalAnswer", query = "SELECT r FROM Response r WHERE r.totalAnswer = :totalAnswer")
    , @NamedQuery(name = "Response.findByAnswer", query = "SELECT r FROM Response r WHERE r.answer = :answer")})
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "response_id")
    private Integer responseId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_answer")
    private int totalAnswer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "answer")
    private String answer;
    @ManyToMany(mappedBy = "responseList")
    private List<Survey> surveyList;

    public Response() {
    }

    public Response(Integer responseId) {
        this.responseId = responseId;
    }

    public Response(Integer responseId, int totalAnswer, String answer) {
        this.responseId = responseId;
        this.totalAnswer = totalAnswer;
        this.answer = answer;
    }

    public Integer getResponseId() {
        return responseId;
    }

    public void setResponseId(Integer responseId) {
        this.responseId = responseId;
    }

    public int getTotalAnswer() {
        return totalAnswer;
    }

    public void setTotalAnswer(int totalAnswer) {
        this.totalAnswer = totalAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    public void setSurveyList(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (responseId != null ? responseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Response)) {
            return false;
        }
        Response other = (Response) object;
        if ((this.responseId == null && other.responseId != null) || (this.responseId != null && !this.responseId.equals(other.responseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Response[ responseId=" + responseId + " ]";
    }
    
}
