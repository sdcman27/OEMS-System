package edu.sru.thangiah.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.sru.thangiah.domain.Exam;
import edu.sru.thangiah.domain.ExamDetails;
import edu.sru.thangiah.domain.ExamQuestion;
import edu.sru.thangiah.domain.ExamQuestionDisplay;
import edu.sru.thangiah.domain.ExamResult;
import edu.sru.thangiah.domain.ExamSubmission;
import edu.sru.thangiah.domain.ExamSubmissionEntity;
import edu.sru.thangiah.domain.Question;

import edu.sru.thangiah.model.Roles;
import edu.sru.thangiah.model.User;
import edu.sru.thangiah.repository.ExamRepository;
import edu.sru.thangiah.repository.ExamSubmissionRepository;
import edu.sru.thangiah.repository.UserRepository;
import edu.sru.thangiah.service.ExamQuestionService;
import edu.sru.thangiah.service.ExamService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;


/**
 * The {@code ExamController} class handles all web requests related to exam management including
 * viewing, creating, updating, and deleting exams, as well as managing exam questions and submissions.
 */
@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;
    
    @Autowired
    private ExamQuestionService examQuestionService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExamSubmissionRepository examSubmissionRepository;
    
    @Autowired
    private ExamRepository examRepository;
    
    public ExamController(ExamService examService) {
        this.examService = examService;
    }
    

    /**
     * Filters exam questions by a specific chapter and adds them to the model for display.
     *
     * @param chapter the chapter number to filter questions by
     * @param model the {@code Model} object to which the filtered questions and chapter list are added
     * @return the name of the view to display the list of filtered exam questions
     */
    @PostMapping("/updateExamName")
    public String updateExamName(@ModelAttribute("examDetails") Exam exam, Model model, HttpSession session) {
        boolean success = examService.updateExamName(exam.getId(), exam.getExamName());
        if (success) {
            model.addAttribute("successMessage", "Exam name updated successfully.");
        }
        return prepareSelectChapterPage(model, session);
    }

    @PostMapping("/updateDuration")
    public String updateDuration(@ModelAttribute("examDetails") Exam exam, Model model, HttpSession session) {
        boolean success = examService.updateDuration(exam.getId(), exam.getDurationInMinutes());
        if (success) {
            model.addAttribute("successMessage", "Exam duration updated successfully.");
        }
        return prepareSelectChapterPage(model, session);
    }

    @PostMapping("/updateStartTime")
    public String updateStartTime(@ModelAttribute("examDetails") Exam exam, Model model, HttpSession session) {
        boolean success = examService.updateStartTime(exam.getId(), exam.getStartTime());
        if (success) {
            exam = examService.getExamById(exam.getId()); // Reload the updated exam
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedStartTime = exam.getStartTime().format(formatter);
            model.addAttribute("formattedStartTime", formattedStartTime);
            model.addAttribute("successMessage", "Start time updated successfully.");
        }
        return prepareSelectChapterPage(model, session);
    }

    @PostMapping("/updateEndTime")
    public String updateEndTime(@ModelAttribute("examDetails") Exam exam, Model model, HttpSession session) {
        boolean success = examService.updateEndTime(exam.getId(), exam.getEndTime());
        if (success) {
            exam = examService.getExamById(exam.getId()); // Reload the updated exam
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedEndTime = exam.getEndTime().format(formatter);
            model.addAttribute("formattedEndTime", formattedEndTime);
            model.addAttribute("successMessage", "End time updated successfully.");
        }
        return prepareSelectChapterPage(model, session);
    }

    private String prepareSelectChapterPage(Model model, HttpSession session) {
        Long examId = (Long) session.getAttribute("currentExamId");
        if (examId == null) {
            return "error"; 
        }

        List<Integer> chapters = examService.getAllChapters();
        model.addAttribute("chapters", chapters);

        Exam exam = examService.getExamById(examId);
        if (exam != null) {
            model.addAttribute("examDetails", exam);
        }


        return "selectChapter"; 
    }
  
    /**
     * Handles the chapter selection for exam generation and updates the session with the selected chapter.
     *
     * @param chapter The chapter number that has been selected.
     * @param session The HTTP session to store the selected chapter number.
     * @param redirectAttributes Attributes for a redirect scenario.
     * @return A redirection to the exam generation page.
     */
    @GetMapping("/selectChapter")
    public String selectChapter(Model model, HttpSession session) {
        Long examId = (Long) session.getAttribute("currentExamId");
        if (examId == null) {
            return "error"; // Handle error scenario
        }

        List<Integer> chapters = examService.getAllChapters();
        model.addAttribute("chapters", chapters);

        Exam exam = examService.getExamById(examId);
        if (exam != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedStartTime = (exam.getStartTime() != null) ? exam.getStartTime().format(formatter) : "";
            String formattedEndTime = (exam.getEndTime() != null) ? exam.getEndTime().format(formatter) : "";

            model.addAttribute("formattedStartTime", formattedStartTime);
            model.addAttribute("formattedEndTime", formattedEndTime);
            model.addAttribute("examName", exam.getExamName());
            model.addAttribute("examDuration", exam.getDurationInMinutes());
            model.addAttribute("examDetails", exam);
        } else {
            model.addAttribute("examDetails", new Exam());
        }

        List<Long> selectedQuestionIds = (List<Long>) session.getAttribute("selectedQuestionIds");
        if (selectedQuestionIds != null && !selectedQuestionIds.isEmpty()) {
            List<ExamQuestion> selectedQuestions = selectedQuestionIds.stream()
                                                                      .map(questionId -> examQuestionService.getExamQuestionById(questionId))
                                                                      .collect(Collectors.toList());
            model.addAttribute("selectedQuestions", selectedQuestions);
        }

        return prepareSelectChapterPage(model, session);
    }

    /**
     * Generates an exam based on a specific chapter and displays it to the user.
     *
     * @param chapter The chapter number from which the exam is generated.
     * @param model The model to add attributes to for the view.
     * @param session The HTTP session to verify the ongoing exam creation process.
     * @return The view name for the exam generation page.
     */
    @PostMapping("/selectChapter")
    public String handleChapterSelection(@RequestParam("selectedChapter") int chapter, 
                                         HttpSession session, RedirectAttributes redirectAttributes) {
        Long examId = (Long) session.getAttribute("currentExamId");
        if (examId == null) {
            return "error"; // Handle error scenario
        }

        // Store the selected chapter in the session
        session.setAttribute("lastSelectedChapter", chapter);

        redirectAttributes.addFlashAttribute("selectedChapter", chapter);
        return "redirect:/exam/generateExam";
    }
    

    @GetMapping("/exam-questions/filterByChapter")
    public String filterExamQuestionsByChapter(@RequestParam("selectedChapter") int chapter, Model model) {
        List<ExamQuestion> questions = examService.generateQuestionsForChapter(chapter);
        model.addAttribute("examQuestions", questions);
        model.addAttribute("chapters", examService.getAllChapters());
        return "listExamQuestions"; 
    }

    /**
     * Retrieves an exam for editing and adds its details to the model.
     *
     * @param id the ID of the exam to be edited
     * @param model the {@code Model} object to which the exam details are added
     * @return the name of the view to edit the exam or a redirect if the exam is not found
     */
    @GetMapping("/edit/{id}")
    public String editExam(@PathVariable Long id, Model model) {
        Exam exam = examService.getExamById(id);
        if (exam == null) {
            return "redirect:/error"; // Or handle the error appropriately
        }

        List<ExamQuestion> allQuestions = examService.getAllExamQuestions();
        model.addAttribute("exam", exam);
        model.addAttribute("allQuestions", allQuestions);
        model.addAttribute("selectedQuestions", exam.getQuestions());

        return "editExam";
    }
    
    /**
     * Updates the questions for a given exam.
     *
     * @param examId the ID of the exam to update
     * @param questionIds the list of question IDs to associate with the exam
     * @return a redirect URL to the exam details page
     */
    @PostMapping("/updateQuestions")
    public String updateExamQuestions(@RequestParam("examId") Long examId, @RequestParam("questionIds") List<Long> questionIds) {
        examService.updateExamQuestions(examId, questionIds);
        return "redirect:/exam/details/" + examId;
    }


    /**
     * Deletes an exam with the given ID.
     *
     * @param id the ID of the exam to be deleted
     * @param redirectAttributes attributes for a redirect scenario
     * @return a redirect URL to the list of all exams
     */
    @GetMapping("/delete/{id}")
    public String deleteExam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = examService.deleteExam(id);
        if (!isDeleted) {
            // Add an attribute indicating the exam couldn't be deleted
            redirectAttributes.addFlashAttribute("deleteError", "Exam with submissions cannot be deleted.");
        }
        return "redirect:/instructor/all-exams";
    }


    /**
     * Retrieves all exam submissions and adds them to the model for viewing.
     *
     * @param model the {@code Model} object to which the submissions are added
     * @return the name of the view to display exam submissions
     */
    @GetMapping("/submissions")
    public String viewExamSubmissions(Model model) {
    	List<ExamSubmissionEntity> submissions = examSubmissionRepository.findAll();
        submissions.forEach(submission -> {
            submission.getExam().getQuestions().size(); 
        });
        model.addAttribute("submissions", submissions);
        return "examSubmissions"; 
    }
    
    /**
     * Displays the details of a specific exam.
     *
     * @param id the ID of the exam to view details for
     * @param model the {@code Model} object to which the exam details are added
     * @return the name of the view to display the exam details or a redirect if the exam is not found
     */
    @GetMapping("/details/{id}")
    public String viewExamDetails(@PathVariable Long id, Model model) {
        Exam exam = examService.getExamById(id); // Implement this method in your service
        if (exam == null) {
            return "redirect:/instructor/all-exams"; // Redirect or show an error page
        }
        model.addAttribute("exam", exam);
        model.addAttribute("generatedExamId", exam.getId());
        model.addAttribute("examDuration", exam.getDurationInMinutes());
        model.addAttribute("selectedQuestions", exam.getQuestions());
        // Add other necessary attributes, like exam questions
        return "examDetails"; 
    }
    
    /**
     * Generates an exam with questions from a specific chapter and of specific types based on the request parameters.
     * The method handles the question generation process and stores the generated exam in the session.
     *
     * @param chapter The chapter number from which to generate the questions.
     * @param numMultipleChoice The number of multiple-choice questions to generate.
     * @param numTrueFalse The number of true/false questions to generate.
     * @param numBlanks The number of fill-in-the-blank questions to generate.
     * @param session The HTTP session to store the current exam ID.
     * @return A {@code ResponseEntity} with the exam ID if successful, or an error message if not.
     * @throws IOException If there is an input/output error during question generation.
     */
    @PostMapping("/manual-auto-generate")
    public ResponseEntity<String> generateExam(
            @RequestParam("chapter") int chapter, 
            @RequestParam("numMultipleChoice") int numMultipleChoice,
            @RequestParam("numTrueFalse") int numTrueFalse,
            @RequestParam("numBlanks") int numBlanks,
            HttpSession session) throws IOException {
        
        Long examId = (Long) session.getAttribute("currentExamId");
        if (examId == null) {
            return ResponseEntity.badRequest().body("No exam initiated for question generation.");
        }

        Optional<Exam> existingExam = examRepository.findById(examId);
        if (!existingExam.isPresent()) {
            return ResponseEntity.badRequest().body("Exam does not exist.");
        }
        Exam exam = existingExam.get();

        List<ExamQuestion> blanksQuestions = examQuestionService.getRandomFillInTheBlanksQuestions(numBlanks);
        List<ExamQuestion> trueFalseQuestions = examQuestionService.getRandomTrueFalseQuestions(numTrueFalse);
        List<ExamQuestion> allQuestionsForChapter = examQuestionService.generateQuestionsForChapter(chapter);
        List<ExamQuestion> multipleChoiceQuestions = allQuestionsForChapter.stream()
                .filter(q -> q.getOptionA() != null && q.getOptionB() != null && q.getOptionC() != null && q.getOptionD() != null)
                .limit(numMultipleChoice)
                .collect(Collectors.toList());

        List<ExamQuestion> combinedQuestions = new ArrayList<>();
        combinedQuestions.addAll(blanksQuestions);
        combinedQuestions.addAll(trueFalseQuestions);
        combinedQuestions.addAll(multipleChoiceQuestions);
        exam.setQuestions(combinedQuestions);

        examRepository.save(exam);

        return ResponseEntity.ok().body(String.valueOf(exam.getId()));
    }

    /**
     * Confirms the creation of an auto-generated exam and presents its details.
     *
     * @param examId The ID of the generated exam.
     * @param model The model to add attributes to for the view.
     * @return The view name for the exam confirmation page.
     */
    @GetMapping("/confirmation/{examId}")
    public String confirmExam(@PathVariable Long examId, Model model) {
        Optional<Exam> exam = examRepository.findById(examId);
        if (!exam.isPresent()) {
            // Handle the case where the exam does not exist
            return "error"; 
        }
        
        model.addAttribute("generatedExamId", examId);
        model.addAttribute("examDetails", exam.get());
        model.addAttribute("selectedQuestions", exam.get().getQuestions());
        return "autoExamConfirmation"; 
    }
    
    /**
     * Adds more chapters to the current exam being generated and updates the session with the selected question IDs.
     *
     * @param examDetails The details of the exam including selected chapters and questions.
     * @param session The HTTP session to store the selected question IDs.
     * @return A redirection to the chapter selection page.
     */
    @PostMapping("/addMoreChapters")
    public String addMoreChapters(@ModelAttribute("examDetails") ExamDetails examDetails,
                                  HttpSession session) {
        List<Long> existingQuestionIds = (List<Long>) session.getAttribute("selectedQuestionIds");
        if (existingQuestionIds == null) {
            existingQuestionIds = new ArrayList<>();
        }

        List<Long> newSelectedQuestionIds = examDetails.getSelectedExamQuestionIds();
        if (newSelectedQuestionIds != null && !newSelectedQuestionIds.isEmpty()) {
            existingQuestionIds.addAll(newSelectedQuestionIds);
        }

        session.setAttribute("selectedQuestionIds", existingQuestionIds);
        System.out.println("Updated question IDs in session: " + existingQuestionIds);

        return "redirect:/exam/selectChapter"; 
    }
    
    /**
     * Finalizes the generation of an exam by combining previously selected questions with the current selection and
     * saves the exam to the repository.
     *
     * @param examDetails The details of the exam including selected chapters and questions.
     * @param session The HTTP session containing the current exam and selected question IDs.
     * @param model The model to add attributes to for the view.
     * @return The view name for the exam generated confirmation page.
     */
    @Transactional
    @PostMapping("/generate")
    public String generateExam(@ModelAttribute("examDetails") ExamDetails examDetails,
                               HttpSession session, Model model) {
        Long examId = (Long) session.getAttribute("currentExamId");
        if (examId == null) {
            System.out.println("Error: Exam ID is null");
            return "error"; // Handle error scenario
        }

        // Retrieve existing question IDs from the session
        List<Long> existingQuestionIds = (List<Long>) session.getAttribute("selectedQuestionIds");
        if (existingQuestionIds == null) {
            existingQuestionIds = new ArrayList<>();
        }

        // Add the currently selected questions to the existing ones
        List<Long> currentSelectedQuestionIds = examDetails.getSelectedExamQuestionIds();
        if (currentSelectedQuestionIds != null && !currentSelectedQuestionIds.isEmpty()) {
            existingQuestionIds.addAll(currentSelectedQuestionIds);
        }

        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (!optionalExam.isPresent()) {
            System.out.println("Error: Exam not found for ID " + examId);
            return "error"; // Handle exam not found scenario
        }
        Exam exam = optionalExam.get();

        if (!existingQuestionIds.isEmpty()) {
            List<ExamQuestion> questions = existingQuestionIds.stream()
                    .distinct() // Remove duplicate question IDs
                    .map(examQuestionService::getExamQuestionById)
                    .collect(Collectors.toList());

            exam.setQuestions(questions);
            examRepository.save(exam);
            System.out.println("Exam saved with questions count: " + exam.getQuestions().size());
        } else {
            System.out.println("No questions selected for the exam.");
        }

        session.removeAttribute("selectedQuestionIds"); // Clear the session attribute

        // Add necessary attributes to the model for the confirmation page
        model.addAttribute("generatedExamId", exam.getId());
        model.addAttribute("examDuration", exam.getDurationInMinutes());
        model.addAttribute("selectedQuestions", exam.getQuestions());

        // Redirect to the confirmation page
        return "examGeneratedConfirmation";
    }


    
    /**
     * Displays the link for an exam so that it can be shared with students.
     *
     * @param id The ID of the exam for which the link is generated.
     * @param model The model to add attributes to for the view.
     * @return The view name for displaying the exam link.
     */
    @GetMapping("/{id}/link")
    public String showExamLink(@PathVariable Long id, Model model) {
        Exam exam = examRepository.findById(id).orElse(null); // Fetching the exam from the database.

        if (exam == null) {
            model.addAttribute("message", "Exam not found");
            return "error-page"; // This should be your error view page.
        }

        // Construct the link that students will use to take the exam. This URL pattern should match your students' exam taking page.
        String examLink = "/exam/take/" + id;

        model.addAttribute("exam", exam); // For displaying other exam details.
        model.addAttribute("examLink", examLink); // The actual link students will use.

        return "display-exam-link"; // This view will display the exam link and details to the faculty.
    }
    
  
    

    @PostMapping("/updateDetails")
    public String updateExamDetails(@ModelAttribute("examDetails") Exam updatedExam) {
        boolean success = examService.updateExamDetails(updatedExam);

        if (success) {
System.out.println("Updated");        
} else {
System.out.println("Error");        }


        return "redirect:/exam/selectChapter"; // Redirect back to the exam page
    }



    
    /**
     * Processes the submission of an exam by a student, evaluates the answers, and displays the score.
     *
     * @param id The ID of the exam being submitted.
     * @param formParams The submitted answers from the form.
     * @param model The model to add attributes to for the view.
     * @param principal The principal to identify the user.
     * @param authentication The authentication to obtain the user's details.
     * @return The view name for displaying the exam score or an error message.
     */
    @GetMapping("/generateExam")
    public String generateExam(@ModelAttribute("selectedChapter") int chapter, Model model, HttpSession session) {
        // Generate questions based on the selected chapter.
        List<ExamQuestion> questions = examService.generateQuestionsForChapter(chapter);
        model.addAttribute("questions", questions);

        // Retrieve the selected question IDs from the session
        List<Long> selectedQuestionIds = (List<Long>) session.getAttribute("selectedQuestionIds");
        if (selectedQuestionIds != null && !selectedQuestionIds.isEmpty()) {
            // Fetch the corresponding ExamQuestion objects
            List<ExamQuestion> selectedQuestions = selectedQuestionIds.stream()
                .map(examQuestionService::getExamQuestionById)
                .collect(Collectors.toList());

            model.addAttribute("selectedQuestions", selectedQuestions);
        }

        return "generateExam"; // This is your Thymeleaf template
    }


    /**
     * Handles the request to take an exam by an examinee and determines if the exam duration is still valid.
     * If the exam is valid and found, it is added to the model and the view for taking the exam is returned.
     * Otherwise, an error message is set and the error view is returned.
     *
     * @param id The ID of the exam to be taken.
     * @param model The {@link Model} to which the exam and message attributes are added.
     * @return The view name for taking the exam or an error page.
     */
    @GetMapping("/{id}")
    public String takeExamz(@PathVariable Long id, Model model) {
        // Retrieve the exam by ID from the repository
        Exam exam = examService.getExamById(id);

        if (exam != null) {
            // Check if the exam's duration is still valid
            if (isExamDurationValid(exam)) {
                model.addAttribute("exam", exam);
                return "takeExam"; 
            } else {
                model.addAttribute("message", "The exam has expired.");
            }
        } else {
            model.addAttribute("message", "Exam not found.");
        }

        return "error"; 
    }
    
    /**
     * Handles the request to take an exam by an examinee and determines if the exam duration is still valid.
     * If the exam is valid and found, it is added to the model and the view for taking the exam is returned.
     * Otherwise, an error message is set and the error view is returned.
     *
     * @param id The ID of the exam to be taken.
     * @param model The {@link Model} to which the exam and message attributes are added.
     * @return The view name for taking the exam or an error page.
     */
    @GetMapping("/take/{id}")
    public String takeExam(@PathVariable Long id, Model model) {
        try {
            Exam exam = examService.getExamById(id);
            if (exam != null) {
                List<ExamQuestion> questions = exam.getQuestions();
                if (questions != null && !questions.isEmpty()) {
                    model.addAttribute("exam", exam);
                    // Log to check if the questions are retrieved successfully
                    System.out.println("Exam with ID " + id + " has the following questions: " + questions);
                    return "takeExam"; // The name of the Thymeleaf template
                } else {
                    // Log for debugging: No questions found for the exam
                    System.out.println("No questions found for the exam with ID " + id);
                    model.addAttribute("message", "No questions available for this exam.");
                }
            } else {
                // Log for debugging: Exam not found
                System.out.println("Exam not found with ID " + id);
                model.addAttribute("message", "Exam not found.");
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.out.println("An error occurred while trying to take the exam with ID " + id + ": " + e.getMessage());
            model.addAttribute("message", "An error occurred while retrieving the exam details.");
        }
        return "error"; // The name of the error view template
    }

    /**
     * Handles the request to take an exam by an examinee and determines if the exam duration is still valid.
     * If the exam is valid and found, it is added to the model and the view for taking the exam is returned.
     * Otherwise, an error message is set and the error view is returned.
     *
     * @param id The ID of the exam to be taken.
     * @param model The {@link Model} to which the exam and message attributes are added.
     * @return The view name for taking the exam or an error page.
     */
    @GetMapping("/exam/{id}/link")
    public String generateExamLink(@PathVariable Long id, Model model) {
        // Get the exam details from the database using the provided id
        Exam exam = examRepository.findById(id).orElse(null);

        // Check if the exam exists
        if (exam == null) {
            // Handle the case where the exam does not exist
            model.addAttribute("message", "The requested exam does not exist.");
            return "error"; // Name of your error view template
        }

        // Create the exam link
        String examLink = "/exam/take/" + id; // Adjust the link based on your URL structure

        // Add attributes to the model
        model.addAttribute("examLink", examLink);
        model.addAttribute("exam", exam);

        // Return the name of the view template that will display the exam link
        return "examLink"; // You need to create this new Thymeleaf template
    }


    /**
     * Processes the submission of an exam by a student, evaluates the answers, and displays the score and any incorrect questions.
     * If the exam is not found, an error message is set and the error page template is returned.
     *
     * @param id The ID of the exam being submitted.
     * @param formParams The map containing the submitted answers from the form.
     * @param model The {@link Model} to which the score and message attributes are added.
     * @param principal The {@link Principal} to identify the user.
     * @param authentication The {@link Authentication} to obtain the user's details.
     * @return The view name for displaying the score or an error page.
     */
@PostMapping("/submit/{id}")
    public String submitExam(@PathVariable Long id, @RequestParam Map<String, String> formParams, Model model, Principal principal, Authentication authentication) {
        Exam exam = examService.getExamById(id);

        if (exam == null) {
            model.addAttribute("message", "Exam not found.");
            return "error";
        }

        Map<Long, String> userAnswers = extractUserAnswers(formParams);
        List<ExamQuestionDisplay> displayQuestions = new ArrayList<>();
        List<ExamQuestionDisplay> incorrectQuestions = new ArrayList<>();
        int totalScore = 0;

        for (ExamQuestion question : exam.getQuestions()) {
            String userAnswerKey = userAnswers.getOrDefault(question.getId(), "No Answer");
            String userAnswerText = mapAnswerKeyToText(question, userAnswerKey);
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswerKey);

            if (isCorrect) {
                totalScore++;
            } else {
                ExamQuestionDisplay incorrectQuestion = new ExamQuestionDisplay();
                incorrectQuestion.setId(question.getId());
                incorrectQuestion.setQuestionText(question.getQuestionText());
                incorrectQuestion.setUserAnswer(userAnswerText);
                incorrectQuestion.setCorrectAnswerText(question.getCorrectAnswerText());
                incorrectQuestions.add(incorrectQuestion);
            }

            ExamQuestionDisplay displayQuestion = new ExamQuestionDisplay();
            displayQuestion.setId(question.getId());
            displayQuestion.setQuestionText(question.getQuestionText());
            displayQuestion.setUserAnswer(userAnswerText);
            displayQuestion.setCorrectAnswerText(question.getCorrectAnswerText());

            displayQuestions.add(displayQuestion);
        }

        ExamSubmission examSubmission = new ExamSubmission();
        examSubmission.setExamId(id);
        examSubmission.setAnswers(new ArrayList<>(userAnswers.values()));
        examSubmission.setScore(totalScore);

        Long userId = getUserIdFromAuthentication(authentication);
        saveExamSubmission(examSubmission, userId);

        model.addAttribute("exam", exam);
        model.addAttribute("score", totalScore);
        model.addAttribute("totalQuestions", exam.getQuestions().size());
        model.addAttribute("displayQuestions", displayQuestions);
        model.addAttribute("incorrectQuestions", incorrectQuestions);

        return "showScore";
    }

private String mapAnswerKeyToText(ExamQuestion question, String answerKey) {
        switch (answerKey) {
            case "A": return question.getOptionA();
            case "B": return question.getOptionB();
            case "C": return question.getOptionC();
            case "D": return question.getOptionD();
            default: return "No Answer";
        }
    }







    private String getAnswerText(ExamQuestion question, String answer) {
        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                return getOptionText(question, answer);
            case TRUE_FALSE:
                return answer.equals("A") ? "True" : "False";
            case FILL_IN_THE_BLANK:
                return answer;
            default:
                return "Invalid Answer";
        }
    }

    
    private String getCorrectAnswerText(ExamQuestion question) {
        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                return getOptionText(question, question.getCorrectAnswer());
            case TRUE_FALSE:
                return question.getCorrectAnswer().equals("A") ? "True" : "False";
            case FILL_IN_THE_BLANK:
                return question.getCorrectAnswer();
            default:
                return "Invalid Answer";
        }
    }

    private String getOptionText(ExamQuestion question, String optionLetter) {
        switch (optionLetter) {
            case "A":
                return question.getOptionA();
            case "B":
                return question.getOptionB();
            case "C":
                return question.getOptionC();
            case "D":
                return question.getOptionD();
            default:
                return "Invalid Option";
        }
    }
    
    private String determineUserRole(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Roles role : user.getRoles()) {
                if ("STUDENT".equals(role.getName())) {
                    return "STUDENT";
                } else if ("INSTRUCTOR".equals(role.getName())) {
                    return "INSTRUCTOR";
                }
                // Add more role checks if needed
            }
        }
        return "UNKNOWN"; // Default or unknown role
    }


    private Map<Long, String> extractUserAnswers(Map<String, String> formParams) {
        Map<Long, String> userAnswers = new HashMap<>();
        for (Map.Entry<String, String> entry : formParams.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("answers[")) {
                Long questionId = Long.parseLong(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
                userAnswers.put(questionId, entry.getValue());
            }
        }
        return userAnswers;
    }



 // Helper method to obtain the authenticated user's ID
 private Long getUserIdFromAuthentication(Authentication authentication) {
     if (authentication != null && authentication.getPrincipal() instanceof User) {
         User user = (User) authentication.getPrincipal();
         return user.getId();
     }
     return null; // Return null if the user ID cannot be obtained
 }

    // Implement this method to get the user's ID
    private Long getUserId() {
        return 1L;
    }

    private boolean isExamDurationValid(Exam exam) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime examEndTime = exam.getStartTime().plusMinutes(exam.getDurationInMinutes());

        // Debugging lines: print times to the console or examine them in your debugger.
        System.out.println("Current Time: " + now);
        System.out.println("Exam Start Time: " + exam.getStartTime());
        System.out.println("Exam End Time: " + examEndTime);

        return now.isBefore(examEndTime);
    }



 // Implement this method to calculate the total score
    private int calculateTotalScore(Exam exam, Map<Long, String> userAnswers) {
        int totalScore = 0;
        List<ExamQuestion> questions = exam.getQuestions();

        // Loop through the questions and compare user answers with correct answers
        for (ExamQuestion question : questions) {
            Long questionId = question.getId();
            String correctAnswer = question.getCorrectAnswer();

            // Retrieve the user's selected answer for this question
            String userAnswer = userAnswers.get(questionId);

            // Check if the user's selected answer matches the correct answer (case-insensitive)
            if (userAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
                totalScore++; // Increment the total score for correct answers
            }
        }

        return totalScore;
    }





 // Placeholder method to save the exam submission to the database or storage
    private void saveExamSubmission(ExamSubmission examSubmission, Long userId) {
        // Create an ExamSubmissionEntity to store the submission details
        ExamSubmissionEntity submissionEntity = new ExamSubmissionEntity();
        
        // Assuming you have an examRepository and a userRepository, retrieve the Exam and User objects
        Exam exam = examRepository.findById(examSubmission.getExamId()).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (exam != null && user != null) {
            // Set the relevant properties of the entity based on the examSubmission object
            submissionEntity.setUser(user);
            submissionEntity.setExam(exam);
            submissionEntity.setSubmissionTime(LocalDateTime.now());
            submissionEntity.setUserAnswers(examSubmission.getAnswers());
            submissionEntity.setScore(examSubmission.getScore());

            // Save the exam submission entity to the database
            examSubmissionRepository.save(submissionEntity);

        } else {

        }
    }
    
    
    @PostMapping("/exam/generate")
    public String generateExam(@ModelAttribute("examDetails") ExamDetails examDetails, RedirectAttributes redirectAttributes) {
        // Assuming examDetails now includes a field for 'chapter' which is populated from the form.
        int chapter = examDetails.getChapter();

        // Fetch selected questions based on the chapter.
        List<ExamQuestion> selectedQuestions = examService.generateQuestionsForChapter(chapter);

        // Create a new exam and set its properties.
        Exam exam = new Exam();
        exam.setExamName(examDetails.getExamName());
        exam.setDurationInMinutes(examDetails.getDurationInMinutes());
        exam.setQuestions(selectedQuestions);

        // Save the exam to the database.
        Exam savedExam = examRepository.save(exam);

        // Add the generated exam's ID as a flash attribute. This makes it available after the redirect.
        redirectAttributes.addFlashAttribute("generatedExamId", savedExam.getId());

        // Redirect to the GET request handler which displays the form.
        return "redirect:/exam/questions"; // Or to the appropriate URL after exam generation.
    }

    
    // This method generates the exam and displays it to the user
    @RequestMapping("/generateExam/{chapter}")
    public String generateExam(Model model, @PathVariable("chapter") int chapter) {
        List<Question> questions = examService.generateExam(chapter, 10);
        model.addAttribute("questions", questions);
        return "exam";
    }
    
    
    @GetMapping("/pickExam/{chapter}")
    public String pickExam(@PathVariable int chapter, Model model) {
        List<Question> questions = examService.generateExam(chapter, 10); // Or however many you want
        model.addAttribute("questions", questions);
        return "edit-exam"; 
    }
    
    /**
     * Submits the edited questions for an exam in Excel format and returns the generated file as a downloadable resource.
     *
     * @param editedQuestions The list of edited questions to be included in the Excel file.
     * @return A {@link ResponseEntity} containing the byte array of the Excel file and the headers for download.
     */
    @PostMapping(value = "/submitEditedExam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> submitEditedExam(@RequestBody List<Question> editedQuestions) {
    	System.out.println("Inside POST");
    	
        byte[] contents = examService.createExcelFile(editedQuestions);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "edited_exam_questions.xlsx";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok()
                .headers(headers)
                .body(contents);
    }


    /**
     * Receives the submitted answers via AJAX, evaluates them, and provides a URL for redirection to the results page.
     *
     * @param userAnswers A map of user answers with question IDs as keys and selected answers as values.
     * @return A map containing the URL for redirection to the results page.
     */
    @PostMapping("/submitAnswers")
    @ResponseBody
    public Map<String, Object> submitAnswers(@RequestBody Map<Integer, String> userAnswers) {
        // Calculate the results based on the user's answers
        ExamResult result = examService.evaluateAnswers(userAnswers);

        // Create a response map to send back as the AJAX response
        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", "/submit-answers"); // URL for redirection

        
        examService.storeExamResultForUser(result);

        return response;
    }

    /**
     * Prepares and displays the results page to the user after an exam has been submitted.
     *
     * @param model The {@link Model} to which the exam result attributes are added.
     * @return The view name for the results page.
     */
    @GetMapping("/submit-answers")
    public String showResults(Model model) {
        // Retrieve the results from the service or session
        ExamResult result = examService.getStoredExamResultForUser();

        // Add results to the model to make them available for the view
        model.addAttribute("score", result.getScore());
        model.addAttribute("correctAnswers", result.getCorrectAnswers());
        model.addAttribute("incorrectAnswers", result.getIncorrectAnswers());
        model.addAttribute("incorrectAnswersWithCorrections", result.getIncorrectAnswersWithCorrections());


        return "submit-answers"; 
    }
}
