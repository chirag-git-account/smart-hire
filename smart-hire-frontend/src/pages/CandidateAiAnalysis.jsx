import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import AiScoreBadge from '../components/AiScoreBadge';
import { Sparkles, CheckCircle2, XCircle, Award, Calendar, RefreshCw, ArrowLeft, Send, HelpCircle, ChevronDown, ChevronUp, Bot, Brain } from 'lucide-react';
import { useToast } from '../components/ToastProvider';

const CandidateAiAnalysis = () => {
  const { applicationId } = useParams();
  const navigate = useNavigate();

  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [rescoring, setRescoring] = useState(false);
  const [showInterviewModal, setShowInterviewModal] = useState(false);
  
  // AI Interview Questions Kit State
  const [interviewKit, setInterviewKit] = useState(null);
  const [loadingKit, setLoadingKit] = useState(false);
  const [expandedQuestions, setExpandedQuestions] = useState({});

  const toast = useToast();

  // Interview Schedule state
  const [interview, setInterview] = useState({
    scheduledAt: '',
    durationMinutes: 45,
    mode: 'ONLINE',
    meetingLink: 'https://meet.google.com/abc-defg-hij',
    feedback: 'Technical Round 1 — System Design & Spring Boot'
  });

  useEffect(() => {
    fetchAnalysis();
  }, [applicationId]);

  const fetchAnalysis = async () => {
    try {
      const res = await api.get(`/ai/analysis/${applicationId}`);
      setAnalysis(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleRescore = async () => {
    setRescoring(true);
    try {
      const res = await api.post(`/ai/analysis/${applicationId}/rescore`);
      setAnalysis(res.data);
      toast.success('Candidate Re-Scored with latest weights!');
    } catch (e) {
      toast.error('Failed to re-score resume');
    } finally {
      setRescoring(false);
    }
  };

  const fetchInterviewKit = async () => {
    setLoadingKit(true);
    try {
      const res = await api.get(`/ai/analysis/${applicationId}/interview-questions`);
      setInterviewKit(res.data);
      toast.success(`Generated ${res.data.questions?.length || 0} tailored interview questions via ${res.data.generatedBy}!`);
    } catch (e) {
      toast.error('Failed to generate interview questions');
    } finally {
      setLoadingKit(false);
    }
  };

  const toggleQuestionExpand = (idx) => {
    setExpandedQuestions(prev => ({
      ...prev,
      [idx]: !prev[idx]
    }));
  };

  const handleScheduleInterview = async (e) => {
    e.preventDefault();
    try {
      await api.post('/interviews/schedule', {
        applicationId: parseInt(applicationId),
        ...interview
      });
      toast.success('Interview Scheduled Successfully!');
      setShowInterviewModal(false);
      fetchAnalysis();
    } catch (e) {
      toast.error('Failed to schedule interview');
    }
  };

  const handleStatusChange = async (newStatus) => {
    try {
      await api.patch(`/recruiter/applications/${applicationId}/status?status=${newStatus}`);
      toast.success(`Application updated to ${newStatus}`);
    } catch (e) {
      toast.error('Failed to update status');
    }
  };

  if (loading) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Generating AI Candidate Assessment...</div>;
  if (!analysis) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Analysis not found</div>;

  return (
    <div style={{ maxWidth: '1000px', margin: '2rem auto', padding: '0 1.5rem' }}>
      <button onClick={() => navigate(-1)} className="btn-secondary btn-sm" style={{ marginBottom: '1.5rem' }}>
        <ArrowLeft size={16} /> Back to Dashboard
      </button>

      {/* Header Profile Card */}
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.4rem' }}>
              <h1 style={{ fontSize: '2rem' }}>{analysis.candidateName}</h1>
              <AiScoreBadge score={analysis.overallScore} recommendation={analysis.recommendation} />
            </div>
            <div style={{ color: 'var(--text-secondary)', fontSize: '1rem' }}>
              Candidate Assessment for <strong>{analysis.jobTitle}</strong>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
            {analysis.resumeId && (
              <>
                <a
                  href={`http://localhost:8080/api/resumes/${analysis.resumeId}/view`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="btn-secondary btn-sm"
                  style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem', color: '#38bdf8', borderColor: 'rgba(56, 189, 248, 0.4)' }}>
                  📄 View Resume PDF
                </a>
                <a
                  href={`http://localhost:8080/api/resumes/${analysis.resumeId}/download`}
                  download
                  className="btn-secondary btn-sm"
                  style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem' }}>
                  📥 Download
                </a>
              </>
            )}
            <button className="btn-secondary btn-sm" onClick={handleRescore} disabled={rescoring}>
              <RefreshCw size={16} className={rescoring ? 'spin' : ''} /> {rescoring ? 'Re-scoring...' : 'Force Re-Score'}
            </button>
            <button className="btn-secondary btn-sm" onClick={fetchInterviewKit} disabled={loadingKit} style={{ background: 'rgba(99, 102, 241, 0.15)', borderColor: 'rgba(99, 102, 241, 0.4)', color: '#a5b4fc' }}>
              <Sparkles size={16} /> {loadingKit ? 'Generating AI Kit...' : 'Generate AI Interview Kit'}
            </button>
            <button className="btn-primary btn-sm" onClick={() => setShowInterviewModal(true)}>
              <Calendar size={16} /> Schedule Interview
            </button>
          </div>
        </div>

        {/* Quick Decision Action Bar */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '1.5rem', paddingTop: '1rem', borderTop: '1px solid var(--border-color)', flexWrap: 'wrap', gap: '0.75rem' }}>
          <div style={{ fontSize: '0.88rem', color: 'var(--text-secondary)' }}>
            Resume File: <strong style={{ color: 'var(--text-primary)' }}>{analysis.resumeFileName || 'Candidate_Resume.pdf'}</strong>
          </div>
          <div style={{ display: 'flex', gap: '0.75rem' }}>
            <button className="btn-secondary btn-sm" onClick={() => handleStatusChange('SHORTLISTED')} style={{ color: '#34d399', borderColor: 'rgba(16, 185, 129, 0.4)' }}>
              Mark Shortlisted
            </button>
            <button className="btn-secondary btn-sm" onClick={() => handleStatusChange('REJECTED')} style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.4)' }}>
              Reject Application
            </button>
          </div>
        </div>
      </div>

      {/* AI Score Breakdown Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '1.5rem', marginBottom: '2rem' }}>
        <div className="glass-card" style={{ padding: '1.5rem' }}>
          <h3 style={{ fontSize: '1.1rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Sparkles size={18} color="var(--accent-primary)" /> Score Weighted Breakdown
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.85rem' }}>
            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.2rem' }}>
                <span>Skill Match (35%)</span>
                <strong>{analysis.skillsScore}/100</strong>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.1)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
                <div style={{ width: `${analysis.skillsScore}%`, background: '#6366f1', height: '100%' }} />
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.2rem' }}>
                <span>Experience Match (25%)</span>
                <strong>{analysis.experienceScore}/100</strong>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.1)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
                <div style={{ width: `${analysis.experienceScore}%`, background: '#10b981', height: '100%' }} />
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.2rem' }}>
                <span>JD Relevance (20%)</span>
                <strong>{analysis.jdRelevanceScore}/100</strong>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.1)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
                <div style={{ width: `${analysis.jdRelevanceScore}%`, background: '#a855f7', height: '100%' }} />
              </div>
            </div>

            <div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.2rem' }}>
                <span>Education Match (20%)</span>
                <strong>{analysis.educationScore}/100</strong>
              </div>
              <div style={{ background: 'rgba(255, 255, 255, 0.1)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
                <div style={{ width: `${analysis.educationScore}%`, background: '#f59e0b', height: '100%' }} />
              </div>
            </div>
          </div>
        </div>

        {/* Matched vs Missing Skills */}
        <div className="glass-card" style={{ padding: '1.5rem' }}>
          <h3 style={{ fontSize: '1.1rem', marginBottom: '1rem' }}>Matched vs Missing Skills</h3>

          <div style={{ marginBottom: '1.25rem' }}>
            <div style={{ fontSize: '0.85rem', color: '#34d399', fontWeight: 700, marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
              <CheckCircle2 size={16} /> Matched Skills ({analysis.matchedSkills.length})
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem' }}>
              {analysis.matchedSkills.map((sk, idx) => (
                <span key={idx} style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', border: '1px solid rgba(16, 185, 129, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.78rem' }}>
                  {sk}
                </span>
              ))}
            </div>
          </div>

          <div>
            <div style={{ fontSize: '0.85rem', color: '#f87171', fontWeight: 700, marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
              <XCircle size={16} /> Missing Skills ({analysis.missingSkills.length})
            </div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem' }}>
              {analysis.missingSkills.length === 0 ? (
                <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>None! Candidate meets all required skills.</span>
              ) : (
                analysis.missingSkills.map((sk, idx) => (
                  <span key={idx} style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', border: '1px solid rgba(239, 68, 68, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.78rem' }}>
                    {sk}
                  </span>
                ))
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Analysis Summary & Strengths */}
      <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem' }}>
        <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>AI Summary Report</h3>
        <p style={{ color: 'var(--text-secondary)', lineHeight: 1.6, marginBottom: '1.5rem', background: 'rgba(0, 0, 0, 0.2)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
          {analysis.analysisSummary}
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem' }}>
          <div>
            <h4 style={{ color: '#34d399', fontSize: '0.95rem', marginBottom: '0.5rem' }}>Key Strengths</h4>
            <ul style={{ paddingLeft: '1.2rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
              {analysis.strengths.map((st, idx) => (
                <li key={idx} style={{ marginBottom: '0.3rem' }}>{st}</li>
              ))}
            </ul>
          </div>

          <div>
            <h4 style={{ color: '#f87171', fontSize: '0.95rem', marginBottom: '0.5rem' }}>Areas of Improvement</h4>
            <ul style={{ paddingLeft: '1.2rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
              {analysis.weaknesses.map((wk, idx) => (
                <li key={idx} style={{ marginBottom: '0.3rem' }}>{wk}</li>
              ))}
            </ul>
          </div>
        </div>
      </div>

      {/* AI Interview Questions Kit Section */}
      {interviewKit && (
        <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem', border: '1px solid rgba(99, 102, 241, 0.4)' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '0.75rem' }}>
            <div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '1.3rem', fontWeight: 700 }}>
                <Bot size={24} color="#818cf8" />
                <span>Tailored AI Interview Questions Kit</span>
              </div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                Curated specifically for {interviewKit.candidateName} for the role of {interviewKit.jobTitle}
              </div>
            </div>
            <span style={{ background: 'rgba(99, 102, 241, 0.15)', color: '#a5b4fc', padding: '0.25rem 0.75rem', borderRadius: 'var(--radius-full)', fontSize: '0.75rem', fontWeight: 600, border: '1px solid rgba(99, 102, 241, 0.3)' }}>
              ⚡ Engine: {interviewKit.generatedBy}
            </span>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {interviewKit.questions.map((q, idx) => {
              const isExpanded = expandedQuestions[idx];
              return (
                <div key={idx} style={{ background: 'rgba(0, 0, 0, 0.25)', border: '1px solid var(--border-color)', borderRadius: 'var(--radius-md)', padding: '1.25rem' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: '1rem', cursor: 'pointer' }} onClick={() => toggleQuestionExpand(idx)}>
                    <div style={{ flex: 1 }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.4rem' }}>
                        <span style={{ background: 'rgba(99, 102, 241, 0.2)', color: '#818cf8', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.7rem', fontWeight: 700 }}>
                          {q.category}
                        </span>
                        <span style={{ background: 'rgba(255, 255, 255, 0.08)', color: 'var(--text-secondary)', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.7rem' }}>
                          Target: {q.targetSkill}
                        </span>
                        <span style={{ background: 'rgba(245, 158, 11, 0.15)', color: '#fbbf24', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.7rem' }}>
                          {q.difficulty}
                        </span>
                      </div>
                      <div style={{ fontWeight: 600, fontSize: '0.95rem', color: 'var(--text-primary)' }}>
                        {idx + 1}. {q.question}
                      </div>
                    </div>
                    <button type="button" style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', padding: '0.25rem' }}>
                      {isExpanded ? <ChevronUp size={18} /> : <ChevronDown size={18} />}
                    </button>
                  </div>

                  {isExpanded && (
                    <div style={{ marginTop: '1rem', paddingTop: '0.85rem', borderTop: '1px solid rgba(255, 255, 255, 0.08)', fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                      <div style={{ color: '#34d399', fontWeight: 600, marginBottom: '0.25rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                        <Brain size={14} /> Expected Answer & Evaluation Criteria:
                      </div>
                      <div style={{ lineHeight: 1.5, background: 'rgba(16, 185, 129, 0.05)', padding: '0.75rem', borderRadius: '6px', border: '1px solid rgba(16, 185, 129, 0.15)' }}>
                        {q.expectedAnswerHint}
                      </div>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* Schedule Interview Modal */}
      {showInterviewModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>Schedule Candidate Interview</h2>
            <form onSubmit={handleScheduleInterview} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Scheduled Date & Time</label>
                <input
                  type="datetime-local"
                  required
                  className="input-field"
                  value={interview.scheduledAt}
                  onChange={(e) => setInterview({ ...interview, scheduledAt: e.target.value })}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Meeting Link (Google Meet / Teams)</label>
                <input
                  type="text"
                  required
                  className="input-field"
                  value={interview.meetingLink}
                  onChange={(e) => setInterview({ ...interview, meetingLink: e.target.value })}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Notes / Agenda</label>
                <input
                  type="text"
                  className="input-field"
                  value={interview.feedback}
                  onChange={(e) => setInterview({ ...interview, feedback: e.target.value })}
                />
              </div>

              <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
                <button type="button" className="btn-secondary" onClick={() => setShowInterviewModal(false)}>Cancel</button>
                <button type="submit" className="btn-primary">Confirm & Send Invite</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CandidateAiAnalysis;
