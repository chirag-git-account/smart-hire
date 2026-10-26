import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('smart_hire_token'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (token) {
      fetchCurrentUser();
    } else {
      setLoading(false);
    }
  }, [token]);

  const fetchCurrentUser = async () => {
    try {
      const response = await api.get('/users/me');
      setUser(response.data);
    } catch (error) {
      console.error('Failed to fetch user profile:', error);
      logout();
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, password) => {
    const response = await api.post('/auth/login', { email, password });
    const { token: jwtToken, ...userData } = response.data;
    localStorage.setItem('smart_hire_token', jwtToken);
    setToken(jwtToken);
    await fetchCurrentUser();
    return userData;
  };

  const register = async (registerData) => {
    const response = await api.post('/auth/register', registerData);
    const { token: jwtToken, ...userData } = response.data;
    localStorage.setItem('smart_hire_token', jwtToken);
    setToken(jwtToken);
    await fetchCurrentUser();
    return userData;
  };

  const logout = () => {
    localStorage.removeItem('smart_hire_token');
    localStorage.removeItem('smart_hire_user');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, fetchCurrentUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
