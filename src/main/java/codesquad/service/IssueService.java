package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import codesquad.dto.IssueDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class IssueService {
    private static final Logger log = LoggerFactory.getLogger(IssueService.class);

    @Resource(name = "issueRepository")
    private IssueRepository issueRepository;

    // TODO issue 대신 issueDto로 전달해야 할 것 같다
    public Issue add(Issue issue, User loginUser) {
        issue.writeBy(loginUser);
        return issueRepository.save(issue);
    }

    public List<Issue> getList() {
        return issueRepository.findAll();
    }

    public Issue get(Long id) {
        log.debug("이슈서비스 이슈 겟 id : {}", id);
        return issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(User loginUser, Long id, IssueDto target) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.update(loginUser, target.applyToIssue());
    }

    @Transactional
    public void delete(User loginUser, Long id) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // 삭제시 이슈 유저가 로그인 유저와 같은지..
        if (!issue.isOwner(loginUser)) {
            throw new UnAuthorizedException();
        }

        // TODO 그냥 리턴?
        if (issue.checkAnswerExist() && !issue.checkAllAnswerWriterIsSameWithWriter()) {
            return;
        }

        issueRepository.delete(issue);
    }

    @Transactional
    public Issue setMilestone(Long id, Milestone milestone) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.milestoneBy(milestone);
        return issue;
    }

    @Transactional
    public Issue setAssignee(Long id, User user) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.assignTo(user);
        return issue;
    }

    @Transactional
    public Issue setLabel(Long id, Label label) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.labelBy(label);
        return issue;
    }

    @Transactional
    public void setFile(Long id, Attachment file) {
        Issue issue = issueRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        issue.attach(file);
    }
}
