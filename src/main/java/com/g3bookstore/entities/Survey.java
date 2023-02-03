package com.g3bookstore.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author shado
 */
@Entity
@Table(name = "survey", catalog = "BOOKSTORE", schema = "")
@NamedQueries({
    @NamedQuery(name = "Survey.findAll", query = "SELECT s FROM Survey s")
    , @NamedQuery(name = "Survey.findBySurveyId", query = "SELECT s FROM Survey s WHERE s.surveyId = :surveyId")
    , @NamedQuery(name = "Survey.findByQuestion", query = "SELECT s FROM Survey s WHERE s.question = :question")
    , @NamedQuery(name = "Survey.findByChosen", query = "SELECT s FROM Survey s WHERE s.chosen = :chosen")})
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "survey_id")
    private Integer surveyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "question")
    private String question;
    @Column(name = "chosen")
    private Boolean chosen;
    @JoinTable(name = "survey_response", joinColumns = {
        @JoinColumn(name = "survey_id", referencedColumnName = "survey_id")}, inverseJoinColumns = {
        @JoinColumn(name = "response_id", referencedColumnName = "response_id")})
    @ManyToMany
    private List<Response> responseList;

    public Survey() {
    }

    public Survey(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public Survey(Integer surveyId, String question) {
        this.surveyId = surveyId;
        this.question = question;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getChosen() {
        return chosen;
    }

    public void setChosen(Boolean chosen) {
        this.chosen = chosen;
    }

    public List<Response> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<Response> responseList) {
        this.responseList = responseList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (surveyId != null ? surveyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Survey)) {
            return false;
        }
        Survey other = (Survey) object;
        if ((this.surveyId == null && other.surveyId != null) || (this.surveyId != null && !this.surveyId.equals(other.surveyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.g3bookstore.entities.Survey[ surveyId=" + surveyId + " ]";
    }
    
}
