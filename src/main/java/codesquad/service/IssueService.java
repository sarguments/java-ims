package codesquad.service;

import codesquad.domain.Issue;
import codesquad.domain.IssueRepository;
import codesquad.domain.Milestone;
import codesquad.domain.User;
import codesquad.dto.IssueDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class IssueService {
    @Resource(name = "issueRepository")
    private IssueRepository issueRepository;

    public Issue add(Issue issue, User loginUser) {
        issue.writeBy(loginUser);
        return issueRepository.save(issue);
    }

    public List<Issue> getList() {
        return issueRepository.findAll();
    }

    public Issue get(Long id) {
        // TODO id가 중복될 경우?
        return issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // TODO toIssue 대체?
    @Transactional
    public void update(User loginUser, Long id, IssueDto target) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.update(loginUser, target.toIssue());
    }

    @Transactional
    public void delete(User loginUser, Long id) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issueRepository.delete(issue);
    }

    @Transactional
    public Issue setMilestone(Long id, Milestone milestone) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.milestoneTo(milestone);
        return issue;
    }
}
