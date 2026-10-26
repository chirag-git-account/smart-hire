import React from 'react';
import { CheckCircle2, Clock, Calendar, AlertCircle } from 'lucide-react';

const steps = ['APPLIED', 'SHORTLISTED', 'INTERVIEW_SCHEDULED', 'HIRED'];
const stepLabels = { APPLIED: 'Applied', SHORTLISTED: 'Shortlisted', INTERVIEW_SCHEDULED: 'Interview', HIRED: 'Selected' };

const ApplicationStatusTimeline = ({ currentStatus }) => {
  if (currentStatus === 'REJECTED' || currentStatus === 'WITHDRAWN') {
    return (
      <div style={{
        display: 'flex',
        alignItems: 'center',
        gap: '0.5rem',
        padding: '0.75rem 1rem',
        background: 'rgba(239, 68, 68, 0.1)',
        border: '1px solid rgba(239, 68, 68, 0.3)',
        borderRadius: 'var(--radius-md)',
        color: '#f87171',
        fontSize: '0.9rem'
      }}>
        <AlertCircle size={18} />
        <span>Application Status: <strong>{currentStatus}</strong></span>
      </div>
    );
  }

  const currentIndex = steps.indexOf(currentStatus);

  return (
    <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', margin: '1rem 0' }}>
      {steps.map((step, idx) => {
        const isCompleted = idx <= currentIndex;
        const isCurrent = idx === currentIndex;

        return (
          <React.Fragment key={step}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '0.35rem' }}>
              <div style={{
                width: '32px',
                height: '32px',
                borderRadius: '50%',
                background: isCompleted ? 'var(--success-color)' : 'rgba(255, 255, 255, 0.1)',
                color: isCompleted ? 'white' : 'var(--text-muted)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontWeight: 700,
                fontSize: '0.85rem',
                boxShadow: isCurrent ? '0 0 15px rgba(16, 185, 129, 0.5)' : 'none'
              }}>
                {isCompleted ? <CheckCircle2 size={18} /> : idx + 1}
              </div>
              <span style={{
                fontSize: '0.75rem',
                fontWeight: isCurrent ? 700 : 500,
                color: isCompleted ? 'var(--text-primary)' : 'var(--text-muted)'
              }}>
                {stepLabels[step] || step}
              </span>
            </div>
            {idx < steps.length - 1 && (
              <div style={{
                flex: 1,
                height: '3px',
                background: idx < currentIndex ? 'var(--success-color)' : 'rgba(255, 255, 255, 0.1)',
                margin: '0 0.5rem',
                borderRadius: '2px'
              }} />
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};

export default ApplicationStatusTimeline;
