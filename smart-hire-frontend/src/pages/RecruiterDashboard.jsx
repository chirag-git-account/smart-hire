import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import AiScoreBadge from '../components/AiScoreBadge';
import { Briefcase, Plus, Users, Sparkles, Filter, Eye, CheckCircle2, Calendar, Edit, Trash2, Building2, Video, Clock, X, Globe, MapPin, FileText, Download, UploadCloud, Search, AlertCircle, ArrowRight } from 'lucide-react';
import { useToast } from '../components/ToastProvider';

const RecruiterDashboard = () => {
  const { user } = useAuth();
  const toast = useToast();
  const [searchParams] = useSearchParams();

  const [activeTab, setActiveTab] = useState('candidates'); // candidates, screener, jobs, interviews, company
  const [stats, setStats] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [selectedJobId, setSelectedJobId] = useState('');
  const [candidates, setCandidates] = useState([]);
  const [interviews, setInterviews] = useState([]);
  const [company, setCompany] = useState({
    name: user?.companyName || '',
    industry: 'Technology & Software',
    location: 'Bengaluru, India',
    website: 'https://' + (user?.companyName ? user.companyName.toLowerCase().replace(/\s+/g, '') + '.com' : 'company.com'),
    employeeSize: '1,000+',
    foundedYear: 2015,
    description: 'Pioneering innovative products and engineering excellence at scale.'
  });

  const [showPostModal, setShowPostModal] = useState(false);
  const [editingJobId, setEditingJobId] = useState(null);
  const [loading, setLoading] = useState(true);

  // Instant Resume Screener State
  const [screenerJobId, setScreenerJobId] = useState('');
  const [screenerFile, setScreenerFile] = useState(null);
  const [screenerLoading, setScreenerLoading] = useState(false);
  const [screenerResult, setScreenerResult] = useState(null);

  // Job Form State
  const [jobForm, setJobForm] = useState({
    title: '',
    description: '',
    location: 'Bengaluru',
    jobType: 'FULL_TIME',
    salaryMin: 1800000,
    salaryMax: 3200000,
    experienceMinYears: 3,
    educationRequirement: 'B.Tech / B.E. / M.Tech in Computer Science',
    responsibilities: '• Design scalable backend microservices\n• Optimize database queries & caching\n• Collaborate with cross-functional teams',
    benefits: '• Health Insurance\n• Performance Bonus\n• Hybrid Work Flexibility',
    deadline: '2026-12-31',
    requiredSkillNames: 'Java, Spring Boot, MySQL, REST APIs, Docker, Microservices'
  });

  useEffect(() => {
    fetchRecruiterData();
    if (searchParams.get('action') === 'post') {
      setShowPostModal(true);
    }
  }, []);

  useEffect(() => {
    if (selectedJobId) {
      fetchJobCandidates(selectedJobId);
    }
  }, [selectedJobId]);

  const fetchRecruiterData = async () => {
    try {
      const [statsRes, jobsRes, interviewsRes] = await Promise.all([
        api.get('/dashboard/recruiter-stats'),
        api.get('/recruiter/jobs'),
        api.get('/interviews/recruiter')
      ]);

      setStats(statsRes.data);
      setJobs(jobsRes.data);
      setInterviews(interviewsRes.data);

      if (jobsRes.data.length > 0) {
        if (!selectedJobId) setSelectedJobId(jobsRes.data[0].id);
        if (!screenerJobId) setScreenerJobId(jobsRes.data[0].id);
      }

      if (user?.companyName) {
        try {
          const compRes = await api.get(`/companies/by-name/${user.companyName}`);
          if (compRes.data) {
            setCompany({
              name: compRes.data.name,
              industry: compRes.data.industry || 'Technology',
              location: compRes.data.location || 'Bengaluru, India',
              website: compRes.data.website || '',
              employeeSize: compRes.data.employeeSize || '1,000+',
              foundedYear: compRes.data.foundedYear || 2015,
              description: compRes.data.description || ''
            });
          }
        } catch (ignored) {}
      }
    } catch (e) {
      console.error('Failed to load recruiter dashboard', e);
    } finally {
      setLoading(false);
    }
  };

  const fetchJobCandidates = async (jobId) => {
    try {
      const res = await api.get(`/recruiter/jobs/${jobId}/candidates`);
      setCandidates(res.data);
    } catch (e) {
      console.error('Failed to load ranked candidates', e);
    }
  };

  const handleSaveJob = async (e) => {
    e.preventDefault();
    try {
      const skillsArray = typeof jobForm.requiredSkillNames === 'string'
        ? jobForm.requiredSkillNames.split(',').map((s) => s.trim())
        : jobForm.requiredSkillNames;

      const payload = {
        ...jobForm,
        requiredSkillNames: skillsArray
      };

      if (editingJobId) {
        await api.put(`/recruiter/jobs/${editingJobId}`, payload);
        toast.success('Job posting updated successfully!');
      } else {
        await api.post('/recruiter/jobs', payload);
        toast.success('New job role published live!');
      }

      setShowPostModal(false);
      setEditingJobId(null);
      fetchRecruiterData();
    } catch (e) {
      toast.error('Failed to save job');
    }
  };

  const handleEditJobClick = (job) => {
    setEditingJobId(job.id);
    setJobForm({
      title: job.title || '',
      description: job.description || '',
      location: job.location || 'Bengaluru',
      jobType: job.jobType || 'FULL_TIME',
      salaryMin: job.salaryMin || 1800000,
      salaryMax: job.salaryMax || 3200000,
      experienceMinYears: job.experienceMinYears || 3,
      educationRequirement: job.educationRequirement || 'B.Tech / B.E.',
      responsibilities: job.responsibilities || '',
      benefits: job.benefits || '',
      deadline: job.deadline || '2026-12-31',
      requiredSkillNames: Array.isArray(job.requiredSkills) ? job.requiredSkills.join(', ') : job.requiredSkills || ''
    });
    setShowPostModal(true);
  };

  const handleDeleteJob = async (jobId) => {
    if (!window.confirm('Are you sure you want to deactivate this job posting?')) return;
    try {
      await api.delete(`/recruiter/jobs/${jobId}`);
      toast.success('Job deactivated successfully.');
      fetchRecruiterData();
    } catch (e) {
      toast.error('Failed to deactivate job');
    }
  };

  const handleStatusUpdate = async (applicationId, newStatus) => {
    try {
      await api.patch(`/recruiter/applications/${applicationId}/status?status=${newStatus}`);
      toast.success(`Candidate status updated to ${newStatus}!`);
      fetchJobCandidates(selectedJobId);
    } catch (e) {
      toast.error('Failed to update status');
    }
  };

  const handleInterviewStatus = async (interviewId, newStatus) => {
    try {
      await api.patch(`/interviews/${interviewId}/status?status=${newStatus}`);
      toast.success(`Interview marked as ${newStatus}`);
      const res = await api.get('/interviews/recruiter');
      setInterviews(res.data);
    } catch (e) {
      toast.error('Failed to update interview');
    }
  };

  const handleCompanyUpdate = async (e) => {
    e.preventDefault();
    try {
      await api.post('/companies', company);
      toast.success('Company Profile updated successfully!');
    } catch (e) {
      toast.error('Failed to update company profile');
    }
  };

  // Instant Resume Screener Handler
  const handleInstantScreen = async (e) => {
    e.preventDefault();
    if (!screenerFile) {
      toast.error('Please select a resume file to screen');
      return;
    }
    setScreenerLoading(true);
    setScreenerResult(null);
    try {
      // 1. Upload resume to parse via Apache Tika
      const formData = new FormData();
      formData.append('file', screenerFile);
      const uploadRes = await api.post('/resumes/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      const resume = uploadRes.data;

      // 2. Find target job
      const targetJob = jobs.find(j => j.id === parseInt(screenerJobId)) || jobs[0];
      const requiredSkills = targetJob?.requiredSkills || ['Java', 'Spring Boot', 'MySQL'];
      const resumeText = (resume.extractedText || '').toLowerCase();

      const matched = [];
      const missing = [];
      requiredSkills.forEach(req => {
        if (resumeText.includes(req.trim().toLowerCase())) {
          matched.push(req.trim());
        } else {
          missing.push(req.trim());
        }
      });

      const skillScore = requiredSkills.length ? Math.round((matched.length / requiredSkills.length) * 100) : 85;
      const candExp = resume.parsedExperienceYears || 3.0;
      const reqExp = targetJob?.experienceMinYears || 2;
      const expScore = candExp >= reqExp ? Math.min(100, 85 + Math.round((candExp - reqExp) * 5)) : Math.max(35, Math.round((candExp / reqExp) * 80));
      const overall = Math.round((skillScore * 0.40) + (expScore * 0.30) + (85 * 0.30));

      setScreenerResult({
        resumeFileName: resume.fileName,
        resumeId: resume.id,
        jobTitle: targetJob?.title || 'Engineering Role',
        overallScore: overall,
        skillScore,
        expScore,
        candidateExp: candExp,
        matchedSkills: matched,
        missingSkills: missing,
        recommendation: overall >= 80 ? 'STRONG_MATCH' : (overall >= 65 ? 'GOOD_MATCH' : (overall >= 50 ? 'MODERATE_MATCH' : 'LOW_MATCH'))
      });
      toast.success('Resume screened successfully!');
    } catch (err) {
      toast.error('Failed to parse and screen resume');
    } finally {
      setScreenerLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '2rem auto', padding: '0 1.5rem' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h1 style={{ fontSize: '2.2rem', marginBottom: '0.4rem' }}>Recruiter Dashboard</h1>
          <p style={{ color: 'var(--text-secondary)' }}>
            Hiring for <strong>{user?.companyName || 'Top Tech'}</strong> • AI candidate scoring & automated pipelines.
          </p>
        </div>

        <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
          <button className="btn-secondary" onClick={() => setActiveTab('screener')}>
            ⚡ Instant Resume Screener
          </button>
          <button className="btn-primary" onClick={() => {
            setEditingJobId(null);
            setJobForm({
              title: '',
              description: '',
              location: 'Bengaluru',
              jobType: 'FULL_TIME',
              salaryMin: 1800000,
              salaryMax: 3200000,
              experienceMinYears: 3,
              educationRequirement: 'B.Tech / B.E. / M.Tech in CS',
              responsibilities: '• Design scalable backend microservices\n• Optimize database queries & caching\n• Collaborate with cross-functional teams',
              benefits: '• Health Insurance\n• Performance Bonus\n• Hybrid Work Flexibility',
              deadline: '2026-12-31',
              requiredSkillNames: 'Java, Spring Boot, MySQL, REST APIs, Docker, Microservices'
            });
            setShowPostModal(true);
          }}>
            <Plus size={18} /> Post New Job
          </button>
        </div>
      </div>

      {/* Metrics Row */}
      {stats && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2rem' }}>
          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(99, 102, 241, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#818cf8' }}>
              <Briefcase size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.totalJobs}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Active Job Roles</div>
            </div>
          </div>

          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(59, 130, 246, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#60a5fa' }}>
              <Users size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.totalApplications}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Total Applicants</div>
            </div>
          </div>

          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(16, 185, 129, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#34d399' }}>
              <CheckCircle2 size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.shortlistedCount}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Shortlisted</div>
            </div>
          </div>

          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(168, 85, 247, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#c084fc' }}>
              <Calendar size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.interviewCount}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Interviews Active</div>
            </div>
          </div>
        </div>
      )}

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '2rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '0.5rem', flexWrap: 'wrap' }}>
        <button
          onClick={() => setActiveTab('candidates')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'candidates' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'candidates' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          AI Candidate Ranking
        </button>

        <button
          onClick={() => setActiveTab('screener')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'screener' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'screener' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          ⚡ Instant Resume Screener
        </button>

        <button
          onClick={() => setActiveTab('jobs')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'jobs' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'jobs' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          My Posted Jobs ({jobs.length})
        </button>

        <button
          onClick={() => setActiveTab('interviews')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'interviews' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'interviews' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          Scheduled Interviews ({interviews.length})
        </button>

        <button
          onClick={() => setActiveTab('company')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'company' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'company' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          Company Profile
        </button>
      </div>

      {/* TAB 1: AI Candidate Ranking */}
      {activeTab === 'candidates' && (
        <div style={{ display: 'grid', gridTemplateColumns: '320px 1fr', gap: '2rem', alignItems: 'flex-start' }}>
          {/* Job Selector Sidebar */}
          <div className="glass-card" style={{ padding: '1.5rem' }}>
            <h3 style={{ fontSize: '1.1rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Filter size={18} /> Select Job Posting
            </h3>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
              {jobs.map((job) => (
                <button
                  key={job.id}
                  onClick={() => setSelectedJobId(job.id)}
                  style={{
                    padding: '0.85rem',
                    textAlign: 'left',
                    borderRadius: 'var(--radius-md)',
                    background: selectedJobId === job.id ? 'rgba(99, 102, 241, 0.25)' : 'rgba(255, 255, 255, 0.03)',
                    border: selectedJobId === job.id ? '1px solid var(--accent-primary)' : '1px solid var(--border-color)',
                    color: 'var(--text-primary)',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}>
                  <div style={{ fontWeight: 600, fontSize: '0.95rem', marginBottom: '0.2rem' }}>{job.title}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                    {job.location} • {job.applicationCount || 0} applicants
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* Ranked Candidates Column */}
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
              <h2 style={{ fontSize: '1.4rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Sparkles size={20} color="var(--accent-primary)" />
                AI Ranked Candidates ({candidates.length})
              </h2>
            </div>

            {candidates.length === 0 ? (
              <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
                No applicants yet for this job opening.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {candidates.map((c, index) => (
                  <div key={c.id} className="glass-card" style={{ padding: '1.5rem' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem', marginBottom: '0.75rem' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                        <div style={{
                          width: '38px',
                          height: '38px',
                          borderRadius: '50%',
                          background: index === 0 ? 'rgba(245, 158, 11, 0.2)' : 'rgba(255, 255, 255, 0.08)',
                          color: index === 0 ? '#f59e0b' : 'var(--text-secondary)',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          fontWeight: 800,
                          fontSize: '1rem'
                        }}>
                          #{index + 1}
                        </div>
                        <div>
                          <h3 style={{ fontSize: '1.2rem', marginBottom: '0.2rem' }}>{c.candidateName}</h3>
                          <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                            {c.candidateEmail} {c.resumeFileName && `• 📄 ${c.resumeFileName}`}
                          </div>
                        </div>
                      </div>

                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', flexWrap: 'wrap' }}>
                        <AiScoreBadge score={c.overallScore} recommendation={c.recommendation} />
                        
                        {c.resumeId && (
                          <>
                            <a
                              href={`http://localhost:8080/api/resumes/${c.resumeId}/view`}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="btn-secondary btn-sm"
                              style={{ display: 'inline-flex', alignItems: 'center', gap: '0.35rem', color: '#38bdf8', borderColor: 'rgba(56, 189, 248, 0.4)' }}>
                              <FileText size={14} /> View Resume
                            </a>
                            <a
                              href={`http://localhost:8080/api/resumes/${c.resumeId}/download`}
                              download
                              className="btn-secondary btn-sm"
                              style={{ display: 'inline-flex', alignItems: 'center', gap: '0.35rem' }}>
                              <Download size={14} />
                            </a>
                          </>
                        )}

                        <Link to={`/candidate-analysis/${c.applicationId}`} className="btn-secondary btn-sm">
                          <Eye size={14} /> AI Assessment
                        </Link>
                      </div>
                    </div>

                    {/* Matched Skills Chips */}
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem', marginTop: '0.5rem', marginBottom: '1rem' }}>
                      {c.matchedSkills?.map((skill, idx) => (
                        <span key={idx} style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', border: '1px solid rgba(16, 185, 129, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.75rem' }}>
                          ✓ {skill}
                        </span>
                      ))}
                    </div>

                    {/* Pipeline Status Action Bar */}
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '0.75rem', borderTop: '1px solid var(--border-color)', fontSize: '0.85rem' }}>
                      <span style={{ color: 'var(--text-secondary)' }}>Status: <strong>{c.applicationStatus}</strong></span>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button className="btn-secondary btn-sm" onClick={() => handleStatusUpdate(c.applicationId, 'SHORTLISTED')} style={{ color: '#34d399', borderColor: 'rgba(16, 185, 129, 0.4)' }}>
                          Shortlist
                        </button>
                        <button className="btn-secondary btn-sm" onClick={() => handleStatusUpdate(c.applicationId, 'REJECTED')} style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.4)' }}>
                          Reject
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {/* TAB 2: Instant Resume Screener */}
      {activeTab === 'screener' && (
        <div style={{ display: 'grid', gridTemplateColumns: '400px 1fr', gap: '2rem', alignItems: 'flex-start' }}>
          {/* Screener Input Box */}
          <div className="glass-card" style={{ padding: '2rem' }}>
            <h2 style={{ fontSize: '1.3rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <UploadCloud size={22} color="var(--accent-primary)" /> Instant Candidate Screener
            </h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem', marginBottom: '1.5rem', lineHeight: 1.5 }}>
              Upload any candidate's resume (PDF, DOCX) to screen against your posted job requirements and get an instant AI compatibility score.
            </p>

            <form onSubmit={handleInstantScreen} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>
                  Target Job Opening
                </label>
                <select
                  className="input-field"
                  value={screenerJobId}
                  onChange={(e) => setScreenerJobId(e.target.value)}>
                  {jobs.map(j => (
                    <option key={j.id} value={j.id}>{j.title} ({j.location})</option>
                  ))}
                </select>
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>
                  Candidate Resume (PDF / DOCX)
                </label>
                <input
                  type="file"
                  accept=".pdf,.doc,.docx,.txt"
                  className="input-field"
                  style={{ padding: '0.5rem' }}
                  onChange={(e) => setScreenerFile(e.target.files[0])}
                />
              </div>

              <button
                type="submit"
                disabled={screenerLoading}
                className="btn-primary"
                style={{ width: '100%', justifyContent: 'center', marginTop: '0.5rem' }}>
                <Sparkles size={18} /> {screenerLoading ? 'Parsing & Scoring...' : 'Screen Candidate Resume'}
              </button>
            </form>
          </div>

          {/* Screener Results Preview */}
          <div>
            {!screenerResult && !screenerLoading && (
              <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-muted)' }}>
                <FileText size={48} style={{ margin: '0 auto 1rem', opacity: 0.4 }} />
                <h3>No Resume Screened Yet</h3>
                <p style={{ fontSize: '0.9rem' }}>Select a target job, upload a resume on the left, and run the screening engine.</p>
              </div>
            )}

            {screenerLoading && (
              <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
                <Sparkles size={48} className="spin" style={{ margin: '0 auto 1rem', color: 'var(--accent-primary)' }} />
                <h3>Analyzing Candidate Qualifications...</h3>
                <p style={{ fontSize: '0.9rem' }}>Running Apache Tika text parser and 4-dimension scoring engine...</p>
              </div>
            )}

            {screenerResult && (
              <div className="glass-card" style={{ padding: '2rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem', marginBottom: '1.5rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem' }}>
                  <div>
                    <h2 style={{ fontSize: '1.5rem', marginBottom: '0.3rem' }}>Screening Result: {screenerResult.resumeFileName}</h2>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                      Target Role: <strong>{screenerResult.jobTitle}</strong>
                    </div>
                  </div>
                  <AiScoreBadge score={screenerResult.overallScore} recommendation={screenerResult.recommendation} />
                </div>

                {/* Score Grid */}
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '1rem', marginBottom: '1.5rem' }}>
                  <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '1rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
                    <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>TECHNICAL SKILLS</div>
                    <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#818cf8' }}>{screenerResult.skillScore}%</div>
                  </div>
                  <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '1rem', borderRadius: '8px', border: '1px solid var(--border-color)' }}>
                    <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>EXPERIENCE MATCH</div>
                    <div style={{ fontSize: '1.4rem', fontWeight: 800, color: '#34d399' }}>{screenerResult.candidateExp} Yrs ({screenerResult.expScore}%)</div>
                  </div>
                </div>

                {/* Skills Analysis */}
                <div style={{ marginBottom: '1.5rem' }}>
                  <h4 style={{ color: '#34d399', fontSize: '0.95rem', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                    <CheckCircle2 size={16} /> Matched Skills ({screenerResult.matchedSkills.length})
                  </h4>
                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem' }}>
                    {screenerResult.matchedSkills.map((sk, idx) => (
                      <span key={idx} style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', border: '1px solid rgba(16, 185, 129, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.78rem' }}>
                        ✓ {sk}
                      </span>
                    ))}
                  </div>
                </div>

                <div>
                  <h4 style={{ color: '#f87171', fontSize: '0.95rem', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                    <AlertCircle size={16} /> Missing Skills ({screenerResult.missingSkills.length})
                  </h4>
                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.4rem' }}>
                    {screenerResult.missingSkills.map((sk, idx) => (
                      <span key={idx} style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', border: '1px solid rgba(239, 68, 68, 0.3)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.78rem' }}>
                        ✗ {sk}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Direct Preview Link */}
                <div style={{ marginTop: '1.5rem', paddingTop: '1rem', borderTop: '1px solid var(--border-color)', display: 'flex', justifyContent: 'flex-end', gap: '0.75rem' }}>
                  <a
                    href={`http://localhost:8080/api/resumes/${screenerResult.resumeId}/view`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="btn-secondary btn-sm"
                    style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem', color: '#38bdf8' }}>
                    📄 Open Parsed Resume PDF
                  </a>
                </div>
              </div>
            )}
          </div>
        </div>
      )}

      {/* TAB 3: My Posted Jobs */}
      {activeTab === 'jobs' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          {jobs.map((job) => (
            <div key={job.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.3rem' }}>
                  <h3 style={{ fontSize: '1.25rem' }}>{job.title}</h3>
                  <span style={{ background: job.isActive ? 'rgba(16, 185, 129, 0.15)' : 'rgba(239, 68, 68, 0.15)', color: job.isActive ? '#34d399' : '#f87171', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.72rem', fontWeight: 700 }}>
                    {job.isActive ? 'ACTIVE' : 'DEACTIVATED'}
                  </span>
                </div>
                <div style={{ display: 'flex', gap: '1rem', color: 'var(--text-secondary)', fontSize: '0.85rem', flexWrap: 'wrap', marginBottom: '0.5rem' }}>
                  <span>📍 {job.location}</span>
                  <span>💼 {job.jobType}</span>
                  <span>💰 ₹{(job.salaryMin / 100000).toFixed(1)}L - ₹{(job.salaryMax / 100000).toFixed(1)}L</span>
                  <span>👥 {job.applicationCount || 0} Applicants</span>
                </div>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.35rem' }}>
                  {job.requiredSkills?.map((sk, idx) => (
                    <span key={idx} style={{ background: 'rgba(99, 102, 241, 0.12)', color: '#a5b4fc', padding: '0.15rem 0.45rem', borderRadius: '4px', fontSize: '0.75rem' }}>
                      {sk}
                    </span>
                  ))}
                </div>
              </div>

              <div style={{ display: 'flex', gap: '0.5rem' }}>
                <button className="btn-secondary btn-sm" onClick={() => handleEditJobClick(job)}>
                  <Edit size={15} /> Edit Role
                </button>
                {job.isActive && (
                  <button className="btn-secondary btn-sm" onClick={() => handleDeleteJob(job.id)} style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.4)' }}>
                    <Trash2 size={15} /> Deactivate
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {/* TAB 4: Scheduled Interviews */}
      {activeTab === 'interviews' && (
        <div>
          {interviews.length === 0 ? (
            <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              No interviews scheduled yet.
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {interviews.map((item) => (
                <div key={item.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.3rem' }}>
                      <h3 style={{ fontSize: '1.2rem' }}>{item.candidateName}</h3>
                      <span style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>({item.candidateEmail})</span>
                      <span style={{
                        background: item.status === 'COMPLETED' ? 'rgba(16, 185, 129, 0.15)' : (item.status === 'CANCELLED' ? 'rgba(239, 68, 68, 0.15)' : 'rgba(99, 102, 241, 0.15)'),
                        color: item.status === 'COMPLETED' ? '#34d399' : (item.status === 'CANCELLED' ? '#f87171' : '#a5b4fc'),
                        padding: '0.15rem 0.5rem',
                        borderRadius: '4px',
                        fontSize: '0.72rem',
                        fontWeight: 700
                      }}>
                        {item.status}
                      </span>
                    </div>

                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.88rem', marginBottom: '0.5rem' }}>
                      Role: <strong>{item.jobTitle}</strong> • Scheduled on {new Date(item.scheduledAt).toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'short' })} ({item.durationMinutes} mins)
                    </div>

                    {item.meetingLink && (
                      <a href={item.meetingLink} target="_blank" rel="noopener noreferrer" style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem', color: '#818cf8', fontSize: '0.85rem', textDecoration: 'underline' }}>
                        <Video size={15} /> Join Google Meet / Meeting Room
                      </a>
                    )}
                  </div>

                  <div style={{ display: 'flex', gap: '0.5rem' }}>
                    {item.status === 'SCHEDULED' && (
                      <>
                        <button className="btn-secondary btn-sm" onClick={() => handleInterviewStatus(item.id, 'COMPLETED')} style={{ color: '#34d399', borderColor: 'rgba(16, 185, 129, 0.4)' }}>
                          Mark Completed
                        </button>
                        <button className="btn-secondary btn-sm" onClick={() => handleInterviewStatus(item.id, 'CANCELLED')} style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.4)' }}>
                          Cancel
                        </button>
                      </>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* TAB 5: Company Profile */}
      {activeTab === 'company' && (
        <form onSubmit={handleCompanyUpdate} className="glass-card" style={{ padding: '2rem', display: 'flex', flexDirection: 'column', gap: '1.25rem', maxWidth: '800px' }}>
          <h2 style={{ fontSize: '1.4rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Building2 size={20} color="var(--accent-primary)" /> Edit Company Profile
          </h2>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Company Name</label>
            <input type="text" required className="input-field" value={company.name} onChange={(e) => setCompany({ ...company, name: e.target.value })} />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Industry</label>
              <input type="text" className="input-field" value={company.industry} onChange={(e) => setCompany({ ...company, industry: e.target.value })} placeholder="e.g. FinTech, Cloud AI, E-Commerce" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Headquarters Location</label>
              <input type="text" className="input-field" value={company.location} onChange={(e) => setCompany({ ...company, location: e.target.value })} placeholder="e.g. Bengaluru, India" />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Website URL</label>
              <input type="text" className="input-field" value={company.website} onChange={(e) => setCompany({ ...company, website: e.target.value })} placeholder="https://example.com" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Employee Size</label>
              <input type="text" className="input-field" value={company.employeeSize} onChange={(e) => setCompany({ ...company, employeeSize: e.target.value })} placeholder="e.g. 5,000+" />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.3rem' }}>Company Overview & Mission</label>
            <textarea className="input-field" rows={4} value={company.description} onChange={(e) => setCompany({ ...company, description: e.target.value })} placeholder="Describe company achievements and tech culture..." />
          </div>

          <button type="submit" className="btn-primary" style={{ alignSelf: 'flex-start' }}>
            Save Company Profile
          </button>
        </form>
      )}

      {/* Post / Edit Job Modal */}
      {showPostModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '650px', maxHeight: '90vh', overflowY: 'auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
              <h2 style={{ fontSize: '1.5rem' }}>{editingJobId ? 'Edit Job Role' : 'Post New Job Role'}</h2>
              <button type="button" onClick={() => setShowPostModal(false)} style={{ background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer' }}>
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSaveJob} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Job Title</label>
                <input
                  type="text"
                  required
                  className="input-field"
                  placeholder="e.g. Senior Java Backend Engineer"
                  value={jobForm.title}
                  onChange={(e) => setJobForm({ ...jobForm, title: e.target.value })}
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Location</label>
                  <input
                    type="text"
                    required
                    className="input-field"
                    placeholder="e.g. Bengaluru (Hybrid)"
                    value={jobForm.location}
                    onChange={(e) => setJobForm({ ...jobForm, location: e.target.value })}
                  />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Work Mode</label>
                  <select
                    className="input-field"
                    value={jobForm.jobType}
                    onChange={(e) => setJobForm({ ...jobForm, jobType: e.target.value })}>
                    <option value="FULL_TIME">Full Time</option>
                    <option value="REMOTE">Remote</option>
                    <option value="HYBRID">Hybrid</option>
                    <option value="INTERNSHIP">Internship</option>
                  </select>
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Min Salary (₹/year)</label>
                  <input
                    type="number"
                    step="100000"
                    className="input-field"
                    value={jobForm.salaryMin}
                    onChange={(e) => setJobForm({ ...jobForm, salaryMin: parseInt(e.target.value) })}
                  />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Max Salary (₹/year)</label>
                  <input
                    type="number"
                    step="100000"
                    className="input-field"
                    value={jobForm.salaryMax}
                    onChange={(e) => setJobForm({ ...jobForm, salaryMax: parseInt(e.target.value) })}
                  />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Min Experience (Years)</label>
                  <input
                    type="number"
                    min="0"
                    className="input-field"
                    value={jobForm.experienceMinYears}
                    onChange={(e) => setJobForm({ ...jobForm, experienceMinYears: parseInt(e.target.value) })}
                  />
                </div>
                <div>
                  <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Education Requirement</label>
                  <input
                    type="text"
                    className="input-field"
                    placeholder="e.g. B.Tech / B.E. in CS"
                    value={jobForm.educationRequirement}
                    onChange={(e) => setJobForm({ ...jobForm, educationRequirement: e.target.value })}
                  />
                </div>
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Required Skills (Comma separated for AI scoring)</label>
                <input
                  type="text"
                  required
                  className="input-field"
                  placeholder="Java, Spring Boot, MySQL, Docker, Kafka"
                  value={jobForm.requiredSkillNames}
                  onChange={(e) => setJobForm({ ...jobForm, requiredSkillNames: e.target.value })}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Job Description</label>
                <textarea
                  className="input-field"
                  rows={3}
                  required
                  placeholder="Overview of the role and technical challenges..."
                  value={jobForm.description}
                  onChange={(e) => setJobForm({ ...jobForm, description: e.target.value })}
                />
              </div>

              <div>
                <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.2rem' }}>Key Responsibilities</label>
                <textarea
                  className="input-field"
                  rows={3}
                  value={jobForm.responsibilities}
                  onChange={(e) => setJobForm({ ...jobForm, responsibilities: e.target.value })}
                />
              </div>

              <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
                <button type="button" className="btn-secondary" onClick={() => setShowPostModal(false)}>Cancel</button>
                <button type="submit" className="btn-primary">{editingJobId ? 'Save Changes' : 'Publish Job'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default RecruiterDashboard;
