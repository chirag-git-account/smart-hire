import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import AiScoreBadge from '../components/AiScoreBadge';
import ApplicationStatusTimeline from '../components/ApplicationStatusTimeline';
import { Briefcase, CheckCircle2, Calendar, FileText, Upload, Sparkles, User, Award, Plus, Trash2, Bot, Brain, Target, Lightbulb, Bookmark, Video, Download, Eye, ExternalLink, Star, GraduationCap, Building } from 'lucide-react';
import { useToast } from '../components/ToastProvider';

const JobSeekerDashboard = () => {
  const { user } = useAuth();
  const toast = useToast();

  const [stats, setStats] = useState(null);
  const [applications, setApplications] = useState([]);
  const [resumes, setResumes] = useState([]);
  const [savedJobs, setSavedJobs] = useState([]);
  const [interviews, setInterviews] = useState([]);
  const [activeTab, setActiveTab] = useState('applications'); // applications, resumes, interviews, saved, profile

  // AI Coaching Feedback Modal State
  const [selectedAppFeedback, setSelectedAppFeedback] = useState(null);
  const [loadingFeedback, setLoadingFeedback] = useState(false);
  const [showFeedbackModal, setShowFeedbackModal] = useState(false);

  // Profile Form state
  const [profile, setProfile] = useState({
    fullName: user?.fullName || '',
    phone: user?.phone || '',
    headline: '',
    summary: '',
    currentLocation: '',
    totalExperienceYears: 0,
    expectedSalary: '',
    noticePeriodDays: 30,
    education: '',
    experienceDetails: '',
    githubUrl: '',
    linkedinUrl: '',
    portfolioUrl: '',
    skills: []
  });

  const [uploading, setUploading] = useState(false);
  const [newSkillName, setNewSkillName] = useState('');

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [statsRes, appsRes, resumesRes, savedRes, interviewsRes, userRes] = await Promise.all([
        api.get('/dashboard/seeker-stats'),
        api.get('/applications/my'),
        api.get('/resumes/my'),
        api.get('/saved-jobs'),
        api.get('/interviews/my'),
        api.get('/users/me')
      ]);

      setStats(statsRes.data);
      setApplications(appsRes.data);
      setResumes(resumesRes.data);
      setSavedJobs(savedRes.data);
      setInterviews(interviewsRes.data);

      const p = userRes.data.profile;
      setProfile({
        fullName: userRes.data.fullName || '',
        phone: userRes.data.phone || '',
        headline: p?.headline || '',
        summary: p?.summary || '',
        currentLocation: p?.currentLocation || '',
        totalExperienceYears: p?.totalExperienceYears || 0,
        expectedSalary: p?.expectedSalary || '',
        noticePeriodDays: p?.noticePeriodDays || 30,
        education: p?.education || '',
        experienceDetails: p?.experienceDetails || '',
        githubUrl: p?.githubUrl || '',
        linkedinUrl: p?.linkedinUrl || '',
        portfolioUrl: p?.portfolioUrl || '',
        skills: p?.skills || []
      });
    } catch (e) {
      console.error('Failed to load seeker dashboard data', e);
    }
  };

  const handleFetchFeedback = async (appId) => {
    setLoadingFeedback(true);
    setShowFeedbackModal(true);
    try {
      const res = await api.get(`/ai/analysis/${appId}/candidate-feedback`);
      setSelectedAppFeedback(res.data);
    } catch (e) {
      toast.error('Failed to generate AI candidate feedback');
      setShowFeedbackModal(false);
    } finally {
      setLoadingFeedback(false);
    }
  };

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      await api.post('/resumes/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      toast.success('Resume parsed and uploaded successfully!');
      fetchDashboardData();
    } catch (e) {
      toast.error('Failed to upload and parse resume');
    } finally {
      setUploading(false);
    }
  };

  const handleSetPrimaryResume = async (resumeId) => {
    try {
      await api.patch(`/resumes/${resumeId}/primary`);
      toast.success('Primary resume updated!');
      fetchDashboardData();
    } catch (e) {
      toast.error('Failed to set primary resume');
    }
  };

  const handleDeleteResume = async (resumeId) => {
    if (!window.confirm('Are you sure you want to delete this resume?')) return;
    try {
      await api.delete(`/resumes/${resumeId}`);
      toast.success('Resume deleted successfully.');
      fetchDashboardData();
    } catch (e) {
      toast.error('Failed to delete resume');
    }
  };

  const handleProfileUpdate = async (e) => {
    e.preventDefault();
    try {
      await api.put('/users/profile', profile);
      toast.success('Profile & Skills updated successfully!');
      fetchDashboardData();
    } catch (e) {
      toast.error('Failed to update profile');
    }
  };

  const handleAddSkill = () => {
    if (!newSkillName.trim()) return;
    if (!profile.skills.includes(newSkillName.trim())) {
      setProfile({
        ...profile,
        skills: [...profile.skills, newSkillName.trim()]
      });
    }
    setNewSkillName('');
  };

  const handleRemoveSkill = (skillToRemove) => {
    setProfile({
      ...profile,
      skills: profile.skills.filter((s) => s !== skillToRemove)
    });
  };

  return (
    <div style={{ maxWidth: '1280px', margin: '2rem auto', padding: '0 1.5rem' }}>
      {/* Header */}
      <div style={{ marginBottom: '2rem' }}>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '0.4rem' }}>Candidate Dashboard</h1>
        <p style={{ color: 'var(--text-secondary)' }}>
          Welcome back, <strong>{user?.fullName}</strong> • Track applications, manage AI parsed resumes, and edit profile.
        </p>
      </div>

      {/* Metrics Row */}
      {stats && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2rem' }}>
          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(99, 102, 241, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#818cf8' }}>
              <Briefcase size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.totalApplications}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Total Applications</div>
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
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Interviews Scheduled</div>
            </div>
          </div>

          <div className="glass-card" style={{ padding: '1.5rem', display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{ background: 'rgba(245, 158, 11, 0.2)', padding: '0.75rem', borderRadius: '12px', color: '#f59e0b' }}>
              <FileText size={24} />
            </div>
            <div>
              <div style={{ fontSize: '1.6rem', fontWeight: 800 }}>{stats.resumeCount}</div>
              <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Active Resumes</div>
            </div>
          </div>
        </div>
      )}

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '2rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '0.5rem', flexWrap: 'wrap' }}>
        <button
          onClick={() => setActiveTab('applications')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'applications' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'applications' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          My Applications ({applications.length})
        </button>

        <button
          onClick={() => setActiveTab('resumes')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'resumes' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'resumes' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          My Resumes ({resumes.length})
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
          Interviews ({interviews.length})
        </button>

        <button
          onClick={() => setActiveTab('saved')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'saved' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'saved' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          Saved Jobs ({savedJobs.length})
        </button>

        <button
          onClick={() => setActiveTab('profile')}
          style={{
            padding: '0.6rem 1.25rem',
            borderRadius: 'var(--radius-md)',
            fontWeight: 600,
            background: activeTab === 'profile' ? 'rgba(99, 102, 241, 0.2)' : 'transparent',
            color: activeTab === 'profile' ? '#818cf8' : 'var(--text-secondary)',
            cursor: 'pointer',
            border: 'none'
          }}>
          Candidate Profile & Skills
        </button>
      </div>

      {/* Tab Content: Applications */}
      {activeTab === 'applications' && (
        <div>
          {applications.length === 0 ? (
            <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              No applications submitted yet. Browse jobs on the <Link to="/jobs" style={{ color: 'var(--accent-primary)', textDecoration: 'underline' }}>Jobs page</Link>.
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
              {applications.map((app) => (
                <div key={app.id} className="glass-card" style={{ padding: '1.75rem' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem', marginBottom: '1.25rem' }}>
                    <div>
                      <h3 style={{ fontSize: '1.3rem', marginBottom: '0.3rem' }}>{app.jobTitle}</h3>
                      <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                        {app.companyName} • Applied on {new Date(app.appliedAt).toLocaleDateString()}
                      </div>
                    </div>

                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', flexWrap: 'wrap' }}>
                      <AiScoreBadge score={app.aiMatchScore} recommendation={app.aiRecommendation} />
                      <button
                        onClick={() => handleFetchFeedback(app.id)}
                        className="btn-secondary btn-sm"
                        style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem', color: '#818cf8' }}>
                        <Sparkles size={15} /> AI Career Coach
                      </button>
                    </div>
                  </div>

                  {/* Stage Timeline */}
                  <ApplicationStatusTimeline currentStatus={app.status} />
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Tab Content: Resumes */}
      {activeTab === 'resumes' && (
        <div>
          {/* Upload Card */}
          <div className="glass-card" style={{ padding: '2rem', marginBottom: '2rem', textAlign: 'center' }}>
            <Upload size={36} color="var(--accent-primary)" style={{ margin: '0 auto 1rem' }} />
            <h3 style={{ fontSize: '1.3rem', marginBottom: '0.5rem' }}>Upload & Parse New Resume</h3>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '1.5rem' }}>
              Supported formats: PDF, DOCX, DOC, TXT (Up to 10MB). Text and experience will be automatically extracted by Apache Tika.
            </p>
            <label className="btn-primary" style={{ cursor: 'pointer', display: 'inline-flex' }}>
              <Plus size={18} /> {uploading ? 'Parsing Resume...' : 'Choose Resume File'}
              <input type="file" accept=".pdf,.doc,.docx,.txt" onChange={handleFileUpload} disabled={uploading} style={{ display: 'none' }} />
            </label>
          </div>

          {/* Resumes List */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {resumes.map((r) => (
              <div key={r.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <div style={{ background: 'rgba(99, 102, 241, 0.15)', padding: '0.75rem', borderRadius: '10px', color: '#818cf8' }}>
                    <FileText size={24} />
                  </div>
                  <div>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem' }}>
                      <h4 style={{ fontSize: '1.1rem' }}>{r.fileName}</h4>
                      {r.isPrimary && (
                        <span style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', padding: '0.15rem 0.5rem', borderRadius: 'var(--radius-full)', fontSize: '0.75rem', fontWeight: 700 }}>
                          PRIMARY
                        </span>
                      )}
                    </div>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                      Parsed Exp: {r.parsedExperienceYears || 0} years • Uploaded on {new Date(r.uploadedAt).toLocaleDateString()}
                    </div>
                  </div>
                </div>

                <div style={{ display: 'flex', gap: '0.6rem' }}>
                  <a
                    href={`http://localhost:8080/api/resumes/${r.id}/view`}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="btn-secondary btn-sm"
                    style={{ display: 'inline-flex', alignItems: 'center', gap: '0.35rem' }}>
                    <Eye size={14} /> View
                  </a>
                  <a
                    href={`http://localhost:8080/api/resumes/${r.id}/download`}
                    download
                    className="btn-secondary btn-sm"
                    style={{ display: 'inline-flex', alignItems: 'center', gap: '0.35rem' }}>
                    <Download size={14} />
                  </a>
                  {!r.isPrimary && (
                    <button
                      className="btn-secondary btn-sm"
                      onClick={() => handleSetPrimaryResume(r.id)}
                      title="Set as Primary">
                      <Star size={14} /> Set Primary
                    </button>
                  )}
                  <button
                    className="btn-secondary btn-sm"
                    onClick={() => handleDeleteResume(r.id)}
                    style={{ color: '#f87171', borderColor: 'rgba(239, 68, 68, 0.3)' }}
                    title="Delete Resume">
                    <Trash2 size={14} />
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Tab Content: Saved Jobs */}
      {activeTab === 'saved' && (
        <div>
          {savedJobs.length === 0 ? (
            <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              No saved jobs. You can bookmark jobs on the <Link to="/jobs" style={{ color: 'var(--accent-primary)', textDecoration: 'underline' }}>Jobs page</Link>.
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {savedJobs.map((item) => (
                <div key={item.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                  <div>
                    <h3 style={{ fontSize: '1.25rem', marginBottom: '0.2rem' }}>{item.job?.title || 'Engineering Role'}</h3>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                      {item.job?.companyName} • {item.job?.location}
                    </div>
                  </div>
                  <Link to={`/jobs/${item.job?.id}`} className="btn-primary btn-sm">
                    View & Apply <ExternalLink size={14} />
                  </Link>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Tab Content: Candidate Profile & Skills */}
      {activeTab === 'profile' && (
        <form onSubmit={handleProfileUpdate} className="glass-card" style={{ padding: '2rem', display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          <h2 style={{ fontSize: '1.4rem', display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
            <User size={22} color="var(--accent-primary)" /> Candidate Profile & Qualifications
          </h2>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Full Name</label>
              <input type="text" required className="input-field" value={profile.fullName} onChange={(e) => setProfile({ ...profile, fullName: e.target.value })} />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Phone Number</label>
              <input type="text" className="input-field" value={profile.phone} onChange={(e) => setProfile({ ...profile, phone: e.target.value })} placeholder="+91 9876543210" />
            </div>
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Professional Headline</label>
            <input type="text" className="input-field" value={profile.headline} onChange={(e) => setProfile({ ...profile, headline: e.target.value })} placeholder="e.g. Senior Java Backend Engineer | Spring Boot, Microservices" />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Professional Summary</label>
            <textarea className="input-field" rows={3} value={profile.summary} onChange={(e) => setProfile({ ...profile, summary: e.target.value })} placeholder="Brief summary of engineering achievements..." />
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Current Location</label>
              <input type="text" className="input-field" value={profile.currentLocation} onChange={(e) => setProfile({ ...profile, currentLocation: e.target.value })} placeholder="e.g. Bengaluru, India" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Total Experience (Years)</label>
              <input type="number" step="0.5" className="input-field" value={profile.totalExperienceYears} onChange={(e) => setProfile({ ...profile, totalExperienceYears: parseFloat(e.target.value) || 0 })} />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Expected Salary</label>
              <input type="text" className="input-field" value={profile.expectedSalary} onChange={(e) => setProfile({ ...profile, expectedSalary: e.target.value })} placeholder="e.g. ₹24,00,000 / year" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Notice Period (Days)</label>
              <input type="number" className="input-field" value={profile.noticePeriodDays} onChange={(e) => setProfile({ ...profile, noticePeriodDays: parseInt(e.target.value) || 0 })} />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Education Details</label>
              <textarea className="input-field" rows={2} value={profile.education} onChange={(e) => setProfile({ ...profile, education: e.target.value })} placeholder="e.g. B.Tech Computer Science - IIT Bombay (2022)" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Past Experience Details</label>
              <textarea className="input-field" rows={2} value={profile.experienceDetails} onChange={(e) => setProfile({ ...profile, experienceDetails: e.target.value })} placeholder="e.g. Software Engineer at Razorpay (2 years)" />
            </div>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>GitHub URL</label>
              <input type="text" className="input-field" value={profile.githubUrl} onChange={(e) => setProfile({ ...profile, githubUrl: e.target.value })} placeholder="https://github.com/username" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>LinkedIn URL</label>
              <input type="text" className="input-field" value={profile.linkedinUrl} onChange={(e) => setProfile({ ...profile, linkedinUrl: e.target.value })} placeholder="https://linkedin.com/in/username" />
            </div>
            <div>
              <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Portfolio URL</label>
              <input type="text" className="input-field" value={profile.portfolioUrl} onChange={(e) => setProfile({ ...profile, portfolioUrl: e.target.value })} placeholder="https://portfolio.dev" />
            </div>
          </div>

          {/* Skills Management */}
          <div>
            <label style={{ display: 'block', fontSize: '0.85rem', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '0.4rem' }}>Technical Skills</label>
            <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '0.75rem' }}>
              <input type="text" className="input-field" placeholder="Add skill (e.g. Kafka, Docker, Kubernetes)" value={newSkillName} onChange={(e) => setNewSkillName(e.target.value)} onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); handleAddSkill(); } }} />
              <button type="button" className="btn-secondary btn-sm" onClick={handleAddSkill}><Plus size={16} /> Add</button>
            </div>

            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
              {profile.skills.map((sk) => (
                <span key={sk} style={{ background: 'rgba(99, 102, 241, 0.15)', color: '#a5b4fc', border: '1px solid rgba(99, 102, 241, 0.3)', padding: '0.25rem 0.65rem', borderRadius: 'var(--radius-full)', fontSize: '0.85rem', display: 'inline-flex', alignItems: 'center', gap: '0.4rem' }}>
                  {sk}
                  <Trash2 size={13} style={{ cursor: 'pointer' }} onClick={() => handleRemoveSkill(sk)} />
                </span>
              ))}
            </div>
          </div>

          <button type="submit" className="btn-primary" style={{ alignSelf: 'flex-start', marginTop: '0.5rem' }}>
            Save Profile & Skills
          </button>
        </form>
      )}

      {/* AI Career Coach Feedback Modal */}
      {showFeedbackModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '700px' }}>
            {loadingFeedback ? (
              <div style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
                <Bot size={36} color="#818cf8" style={{ marginBottom: '1rem', animation: 'spin 2s linear infinite' }} />
                <div>Generating Personalized AI Career Feedback...</div>
              </div>
            ) : selectedAppFeedback ? (
              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
                  <div>
                    <h2 style={{ fontSize: '1.4rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                      <Sparkles size={20} color="#818cf8" />
                      AI Career Coach Evaluation
                    </h2>
                    <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                      Role: <strong>{selectedAppFeedback.jobTitle}</strong> • Compatibility Score: <strong>{selectedAppFeedback.overallScore}%</strong>
                    </div>
                  </div>
                  <span style={{ background: 'rgba(99, 102, 241, 0.15)', color: '#a5b4fc', padding: '0.25rem 0.65rem', borderRadius: 'var(--radius-full)', fontSize: '0.7rem', border: '1px solid rgba(99, 102, 241, 0.3)' }}>
                    {selectedAppFeedback.generatedBy}
                  </span>
                </div>

                <div style={{ background: 'rgba(0, 0, 0, 0.25)', padding: '1rem', borderRadius: 'var(--radius-md)', marginBottom: '1.25rem', lineHeight: 1.6, fontSize: '0.9rem', color: 'var(--text-primary)' }}>
                  {selectedAppFeedback.executiveSummary}
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem', marginBottom: '1.25rem' }}>
                  <div style={{ background: 'rgba(16, 185, 129, 0.05)', border: '1px solid rgba(16, 185, 129, 0.2)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                    <div style={{ color: '#34d399', fontWeight: 700, fontSize: '0.85rem', marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                      <Award size={15} /> Key Strengths
                    </div>
                    <ul style={{ paddingLeft: '1.1rem', fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                      {selectedAppFeedback.topStrengths?.map((st, idx) => (
                        <li key={idx} style={{ marginBottom: '0.25rem' }}>{st}</li>
                      ))}
                    </ul>
                  </div>

                  <div style={{ background: 'rgba(245, 158, 11, 0.05)', border: '1px solid rgba(245, 158, 11, 0.2)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                    <div style={{ color: '#fbbf24', fontWeight: 700, fontSize: '0.85rem', marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                      <Target size={15} /> Recommended Next Skills
                    </div>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.3rem' }}>
                      {selectedAppFeedback.recommendedNextSkills?.map((sk, idx) => (
                        <span key={idx} style={{ background: 'rgba(245, 158, 11, 0.15)', color: '#fbbf24', padding: '0.15rem 0.45rem', borderRadius: '4px', fontSize: '0.75rem' }}>
                          {sk}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>

                <div style={{ marginBottom: '1.25rem' }}>
                  <div style={{ fontWeight: 700, fontSize: '0.85rem', color: 'var(--text-primary)', marginBottom: '0.4rem', display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                    <Lightbulb size={15} color="#818cf8" /> Resume Improvement Tips:
                  </div>
                  <ul style={{ paddingLeft: '1.2rem', fontSize: '0.85rem', color: 'var(--text-secondary)', lineHeight: 1.5 }}>
                    {selectedAppFeedback.resumeImprovementTips?.map((tip, idx) => (
                      <li key={idx} style={{ marginBottom: '0.3rem' }}>{tip}</li>
                    ))}
                  </ul>
                </div>

                {selectedAppFeedback.interviewPrepTip && (
                  <div style={{ background: 'rgba(99, 102, 241, 0.1)', border: '1px solid rgba(99, 102, 241, 0.3)', padding: '0.85rem 1rem', borderRadius: 'var(--radius-md)', fontSize: '0.85rem', color: '#c7d2fe', marginBottom: '1.5rem' }}>
                    <strong>💡 AI Interview Preparation Tip:</strong> {selectedAppFeedback.interviewPrepTip}
                  </div>
                )}

                <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                  <button type="button" className="btn-secondary" onClick={() => setShowFeedbackModal(false)}>Close</button>
                </div>
              </div>
            ) : null}
          </div>
        </div>
      )}
    </div>
  );
};

export default JobSeekerDashboard;
