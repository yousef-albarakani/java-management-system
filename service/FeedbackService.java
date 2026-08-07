package service;

import java.time.LocalDate;
import java.util.ArrayList;

import model.Comment;
import model.Feedback;
import util.FileUtil;
import util.IdGenerator;
import util.ValidationUtil;

public class FeedbackService {

    private final String feedbacksFile = "src/data/feedbacks.txt";
    private final String commentsFile  = "src/data/comments.txt";

    public boolean addFeedback(Feedback feedback) {
        if (feedback == null) return false;
        if (!isFeedbackValid(feedback)) return false;

        feedback.setFeedbackId(IdGenerator.generateNextId("F", feedbacksFile));
        FileUtil.appendLine(feedbacksFile, feedback.toFileString());
        return true;
    }

    public boolean addComment(Comment comment) {
        if (comment == null) return false;
        if (!isCommentValid(comment)) return false;

        comment.setCommentId(IdGenerator.generateNextId("CM", commentsFile));

        if (ValidationUtil.isEmpty(comment.getDate())) {
            comment.setDate(LocalDate.now().toString());
        }

        FileUtil.appendLine(commentsFile, comment.toFileString());
        return true;
    }

    public ArrayList<Feedback> getAllFeedbacks() {
        ArrayList<Feedback> feedbackList = new ArrayList<>();
        ArrayList<String> lines = FileUtil.readAllLines(feedbacksFile);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;

            String[] p = line.split("\\|");
            if (p.length < 7) continue;  // skip malformed rows

            try {
                int rating = Integer.parseInt(p[6].trim());
                Feedback feedback = new Feedback(
                    p[0].trim(), // feedbackId
                    p[1].trim(), // appointmentId
                    p[2].trim(), // technicianId
                    p[3].trim(), // technicianName
                    p[4].trim(), // customerId
                    p[5].trim(), // feedbackText
                    rating       // rating
                );
                feedbackList.add(feedback);
            } catch (NumberFormatException e) {
                // Skip rows where rating column is not a valid integer
                System.err.println("[FeedbackService] Skipping malformed line: " + line);
            }
        }

        return feedbackList;
    }

    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> commentList = new ArrayList<>();
        ArrayList<String> lines = FileUtil.readAllLines(commentsFile);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;

            String[] p = line.split("\\|");
            if (p.length < 7) continue;

            commentList.add(new Comment(
                p[0].trim(), p[1].trim(), p[2].trim(),
                p[3].trim(), p[4].trim(), p[5].trim(), p[6].trim()
            ));
        }

        return commentList;
    }

    public ArrayList<Feedback> getFeedbackByCustomer(String customerId) {
        ArrayList<Feedback> result = new ArrayList<>();

        for (Feedback feedback : getAllFeedbacks()) {
            if (feedback.getCustomerId().equalsIgnoreCase(customerId)) {
                result.add(feedback);
            }
        }

        return result;
    }

    private boolean isFeedbackValid(Feedback feedback) {
        return !ValidationUtil.isEmpty(feedback.getAppointmentId())  &&
               !ValidationUtil.isEmpty(feedback.getTechnicianId())   &&
               !ValidationUtil.isEmpty(feedback.getTechnicianName()) &&
               !ValidationUtil.isEmpty(feedback.getCustomerId())     &&
               !ValidationUtil.isEmpty(feedback.getFeedbackText())   &&
               ValidationUtil.isValidRating(feedback.getRating());
    }

    private boolean isCommentValid(Comment comment) {
        return !ValidationUtil.isEmpty(comment.getAppointmentId())        &&
               !ValidationUtil.isEmpty(comment.getCustomerId())           &&
               !ValidationUtil.isEmpty(comment.getCustomerName())         &&
               !ValidationUtil.isEmpty(comment.getStaffOrTechnicianId())  &&
               !ValidationUtil.isEmpty(comment.getCommentText());
    }
}