import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Building2, MapPin, Users, Globe, Calendar, Briefcase, ArrowLeft, ChevronRight, CheckCircle2 } from 'lucide-react';

const CompanyDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [company, setCompany] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchCompanyDetails();
  }, [id]);

  const fetchCompanyDetails = async () => {
    try {
      const res = await api.get(`/companies/${id}`);
      setCompany(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Loading company profile...</div>;
  if (!company) return <div style={{ textAlign: 'center', padding: '4rem', color: 'var(--text-muted)' }}>Company not found</div>;

  return (
    <div style={{ maxWidth: '1100px', margin: '2rem auto', padding: '0 1.5rem' }}>
      <button onClick={() => navigate(-1)} className="btn-secondary btn-sm" style={{ marginBottom: '1.5rem' }}>
        <ArrowLeft size={16} /> Back to Companies
      </button>

      {/* Header Profile */}
      <div className="glass-card" style={{ padding: '2.5rem', marginBottom: '2rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1.5rem' }}>
          <div style={{ display: 'flex', gap: '1.25rem', alignItems: 'center' }}>
            <div style={{
              width: '64px',
              height: '64px',
              borderRadius: '16px',
              background: 'rgba(99, 102, 241, 0.2)',
              border: '1px solid rgba(99, 102, 241, 0.4)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#818cf8'
            }}>
              <Building2 size={32} />
            </div>
            <div>
              <h1 style={{ fontSize: '2.2rem', marginBottom: '0.2rem' }}>{company.name}</h1>
              <div style={{ color: 'var(--accent-primary)', fontWeight: 600, fontSize: '0.95rem' }}>
                {company.industry || 'Technology'}
              </div>
            </div>
          </div>

          {company.website && (
            <a
              href={company.website}
              target="_blank"
              rel="noopener noreferrer"
              className="btn-secondary btn-sm"
              style={{ display: 'inline-flex', alignItems: 'center', gap: '0.4rem' }}
            >
              <Globe size={15} /> Visit Website
            </a>
          )}
        </div>

        <p style={{ color: 'var(--text-secondary)', fontSize: '1rem', lineHeight: 1.6, marginTop: '1.5rem', marginBottom: '1.5rem' }}>
          {company.description}
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '1rem', paddingTop: '1.25rem', borderTop: '1px solid var(--border-color)' }}>
          {company.location && (
            <div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>Headquarters</div>
              <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{company.location}</div>
            </div>
          )}
          {company.employeeSize && (
            <div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>Company Size</div>
              <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{company.employeeSize} Employees</div>
            </div>
          )}
          {company.foundedYear && (
            <div>
              <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>Founded</div>
              <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{company.foundedYear}</div>
            </div>
          )}
          <div>
            <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>Active Open Roles</div>
            <div style={{ fontWeight: 600, fontSize: '0.9rem', color: '#34d399' }}>{company.activeJobCount} Openings</div>
          </div>
        </div>
      </div>

      {/* Active Jobs Section */}
      <div>
        <h2 style={{ fontSize: '1.5rem', marginBottom: '1.25rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Briefcase size={20} color="var(--accent-primary)" />
          Open Positions at {company.name} ({company.activeJobs?.length || 0})
        </h2>

        {!company.activeJobs || company.activeJobs.length === 0 ? (
          <div className="glass-card" style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
            No open roles currently posted by {company.name}. Check back soon!
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {company.activeJobs.map((job) => (
              <div key={job.id} className="glass-card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                <div>
                  <h3 style={{ fontSize: '1.25rem', marginBottom: '0.3rem' }}>{job.title}</h3>
                  <div style={{ display: 'flex', gap: '1rem', color: 'var(--text-secondary)', fontSize: '0.85rem', flexWrap: 'wrap', marginBottom: '0.75rem' }}>
                    <span>📍 {job.location}</span>
                    <span>💼 {job.jobType}</span>
                    {job.salaryMax && <span>💰 ₹{(job.salaryMin / 100000).toFixed(1)}L - ₹{(job.salaryMax / 100000).toFixed(1)}L</span>}
                    <span>⏳ {job.experienceMinYears}+ yrs exp</span>
                  </div>

                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.35rem' }}>
                    {job.requiredSkills?.map((sk, idx) => (
                      <span key={idx} style={{ background: 'rgba(99, 102, 241, 0.12)', color: '#a5b4fc', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.75rem' }}>
                        {sk}
                      </span>
                    ))}
                  </div>
                </div>

                <Link to={`/jobs/${job.id}`} className="btn-primary btn-sm">
                  View Role & Apply <ChevronRight size={15} />
                </Link>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CompanyDetails;
