import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Building2, MapPin, Briefcase, Calendar, DollarSign, Bookmark, Send, Sparkles, CheckCircle2, AlertCircle } from 'lucide-react';

const JobDetails = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [job, setJob] = useState(null);
  const [resumes, setResumes] = useState([]);
  const [selectedResumeId, setSelectedResumeId] = useState('');
  const [coverLetter, setCoverLetter] = useState('');
  const [showApplyModal, setShowApplyModal] = useState(false);
  const [applied, setApplied] = useState(false);
  const [saved, setSaved] = useState(false);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchJobDetails();
    if (user && user.role === 'JOB_SEEKER') {
      fetchResumes();
    }
  }, [id, user]);

  const fetchJobDetails = async () => {
    try {
      const res = await api.get(`/jobs/${id}`);
      setJob(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const fetchResumes = async () => {
    try {
      const res = await api.get('/resumes/my');
      setResumes(res.data);
      if (res.data.length > 0) {
        setSelectedResumeId(res.data[0].id);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveJob = async () => {
    if (!user) {
      navigate('/login');
      return;
    }
    try {
      await api.post(`/saved-jobs/${id}`);
      setSaved(true);
    } catch (e) {
      console.error(e);
    }
  };

  const handleApply = async (e) => {
    e.preventDefault();
    if (!selectedResumeId) {
      setError('Please select a resume or upload one from your dashboard.');
      return;
    }
    setSubmitting(true);
    setError('');

    try {
      await api.post('/applications', {
        jobId: parseInt(id),
        resumeId: parseInt(selectedResumeId),
        coverLetter: coverLetter
      });
      setApplied(true);
      setShowApplyModal(false);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit application');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Loading job details...</div>;
  if (!job) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Job not found</div>;

  return (
    <div style={{ maxWidth: '1000px', margin: '2rem auto', padding: '0 1.5rem' }}>
      <div className="glass-card" style={{ padding: '2.5rem', marginBottom: '2rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem', marginBottom: '1.5rem' }}>
          <div>
            <h1 style={{ fontSize: '2.2rem', marginBottom: '0.4rem' }}>{job.title}</h1>
            <div style={{ color: 'var(--text-secondary)', fontSize: '1.05rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Building2 size={18} /> <strong>{job.companyName}</strong> • <MapPin size={16} /> {job.location}
            </div>
          </div>

          <div style={{ display: 'flex', gap: '0.75rem' }}>
            <button className="btn-secondary" onClick={handleSaveJob} style={{ opacity: saved ? 0.6 : 1 }}>
              <Bookmark size={18} color={saved ? '#10b981' : 'currentColor'} /> {saved ? 'Saved' : 'Save'}
            </button>
            {user && user.role === 'JOB_SEEKER' && (
              <button className="btn-primary" disabled={applied} onClick={() => setShowApplyModal(true)}>
                <Send size={18} /> {applied ? 'Applied' : 'Apply Now'}
              </button>
            )}
          </div>
        </div>

        {applied && (
          <div style={{ background: 'rgba(16, 185, 129, 0.15)', border: '1px solid rgba(16, 185, 129, 0.4)', color: '#34d399', padding: '1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <CheckCircle2 size={22} />
            <div>
              <strong>Application Submitted Successfully!</strong>
              <div style={{ fontSize: '0.85rem' }}>Our AI scoring engine is evaluating your resume match score. Check status in your dashboard.</div>
            </div>
          </div>
        )}

        {/* Highlights Bar */}
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: '1rem', background: 'rgba(0, 0, 0, 0.2)', padding: '1.25rem', borderRadius: 'var(--radius-md)', marginBottom: '2rem' }}>
          <div>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>SALARY RANGE</div>
            <div style={{ color: 'var(--success-color)', fontWeight: 700 }}>₹{(job.salaryMin / 100000).toFixed(1)}L - {(job.salaryMax / 100000).toFixed(1)}L</div>
          </div>
          <div>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>JOB TYPE</div>
            <div style={{ fontWeight: 600 }}>{job.jobType}</div>
          </div>
          <div>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>MIN EXPERIENCE</div>
            <div style={{ fontWeight: 600 }}>{job.experienceMinYears} Years</div>
          </div>
          <div>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>DEADLINE</div>
            <div style={{ fontWeight: 600 }}>{job.deadline ? job.deadline : 'Open Until Filled'}</div>
          </div>
        </div>

        {/* Required Skills */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1.1rem', marginBottom: '0.75rem' }}>Required Technical Skills</h3>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
            {job.requiredSkills.map((sk, idx) => (
              <span key={idx} style={{ background: 'rgba(99, 102, 241, 0.15)', color: '#a5b4fc', border: '1px solid rgba(99, 102, 241, 0.3)', padding: '0.3rem 0.75rem', borderRadius: 'var(--radius-md)', fontSize: '0.85rem', fontWeight: 600 }}>
                {sk}
              </span>
            ))}
          </div>
        </div>

        {/* Description */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>About the Role</h3>
          <p style={{ color: 'var(--text-secondary)', lineHeight: 1.7, whiteSpace: 'pre-line' }}>{job.description}</p>
        </div>

        {/* Responsibilities */}
        {job.responsibilities && (
          <div style={{ marginBottom: '2rem' }}>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>Responsibilities</h3>
            <p style={{ color: 'var(--text-secondary)', lineHeight: 1.7, whiteSpace: 'pre-line' }}>{job.responsibilities}</p>
          </div>
        )}

        {/* Education & Benefits */}
        {job.benefits && (
          <div>
            <h3 style={{ fontSize: '1.2rem', marginBottom: '0.75rem' }}>Perks & Benefits</h3>
            <p style={{ color: 'var(--text-secondary)', lineHeight: 1.7, whiteSpace: 'pre-line' }}>{job.benefits}</p>
          </div>
        )}
      </div>

      {/* Apply Modal */}
      {showApplyModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2 style={{ fontSize: '1.5rem', marginBottom: '0.5rem' }}>Apply for {job.title}</h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
              Select your resume for AI parsing and scoring against job requirements.
            </p>

            {error && (
              <div style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '0.75rem', borderRadius: 'var(--radius-md)', marginBottom: '1rem', fontSize: '0.85rem' }}>
                {error}
              </div>
            )}

            <form onSubmit={handleApply} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Select Resume</label>
                {resumes.length === 0 ? (
                  <div style={{ color: 'var(--warning-color)', fontSize: '0.9rem' }}>
                    No resumes found! Please upload a PDF resume from your dashboard first.
                  </div>
                ) : (
                  <select className="input-field" value={selectedResumeId} onChange={(e) => setSelectedResumeId(e.target.value)}>
                    {resumes.map((r) => (
                      <option key={r.id} value={r.id}>
                        {r.fileName} {r.isPrimary ? '(Primary)' : ''}
                      </option>
                    ))}
                  </select>
                )}
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Cover Letter (Optional)</label>
                <textarea
                  className="input-field"
                  rows={4}
                  placeholder="Explain why your technical background makes you a strong fit for this position..."
                  value={coverLetter}
                  onChange={(e) => setCoverLetter(e.target.value)}
                />
              </div>

              <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
                <button type="button" className="btn-secondary" onClick={() => setShowApplyModal(false)}>Cancel</button>
                <button type="submit" className="btn-primary" disabled={submitting || resumes.length === 0}>
                  <Sparkles size={16} /> {submitting ? 'Analyzing & Applying...' : 'Submit Application'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default JobDetails;
