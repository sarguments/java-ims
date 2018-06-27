package codesquad.service;

import codesquad.domain.Issue;
import codesquad.domain.IssueRepository;
import codesquad.domain.User;
import org.springframework.stereotype.Service;

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
}
