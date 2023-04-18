package no.kristiania.db;

public class QuestionOptions {

 Long optionId;
 String questionOptionsTitle;
 String optionDescription;


 public Long getOptionId() {
  return optionId;
 }

 public void setOptionId(Long optionId) {
  this.optionId = optionId;
 }

 public String getOptionDescription() {
  return optionDescription;
 }

 public void setOptionDescription(String optionDescription) {
  this.optionDescription = optionDescription;
 }

 public String getQuestionOptionsTitle() {
  return questionOptionsTitle;
 }

 public void setQuestionOptionsTitle(String questionOptionsTitle) {
  this.questionOptionsTitle = questionOptionsTitle;
 }
}

