import React, { useState } from 'react';
import axios from 'axios';

function Chatbot({ userId }) {
  const [messages, setMessages] = useState([
    { from: 'bot', text: 'Bonjour, je suis votre assistant RH. Comment puis-je vous aider ?' },
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const normalizeText = (text) => {
    return text.replace(/\s+/g, ' ').trim();
  };

  const sendMessage = async () => {
    const text = input.trim();
    if (!text || loading) return;

    setMessages((prev) => [...prev, { from: 'user', text }]);
    setInput('');
    setLoading(true);

    try {
      const res = await axios.post('http://localhost:8080/api/chatbot/ask', {
        message: text,
        userId,
      });

      const botText = normalizeText(res.data.answer || '');
      setMessages((prev) => [...prev, { from: 'bot', text: botText }]);
    } catch (err) {
      console.error(err);
      setMessages((prev) => [
        ...prev,
        {
          from: 'bot',
          text: "Désolé, une erreur s'est produite côté serveur. Merci de réessayer plus tard.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div style={styles.wrapper}>
      <div style={styles.container}>
        {/* Header */}
        <div style={styles.header}>
          <div style={styles.headerLeft}>
            <div style={styles.avatar}>
              <span style={styles.avatarText}>RH</span>
            </div>
            <div>
              <div style={styles.title}>Assistant RH</div>
              <div style={styles.subtitle}>
                <span style={styles.statusDot} /> En ligne pour vous aider
              </div>
            </div>
          </div>
        </div>

        {/* Zone de messages */}
        <div style={styles.messagesContainer}>
          <div style={styles.messages}>
            {messages.map((m, i) => (
              <div
                key={i}
                style={{
                  ...styles.messageRow,
                  justifyContent: m.from === 'user' ? 'flex-end' : 'flex-start',
                }}
              >
                {m.from === 'bot' && (
                  <div style={styles.smallAvatar}>
                    <span style={styles.smallAvatarText}>R</span>
                  </div>
                )}
                <div
                  style={{
                    ...styles.messageBubble,
                    ...(m.from === 'user' ? styles.userBubble : styles.botBubble),
                  }}
                >
                  {m.text}
                </div>
              </div>
            ))}
            {loading && (
              <div style={styles.messageRow}>
                <div style={styles.smallAvatar}>
                  <span style={styles.smallAvatarText}>R</span>
                </div>
                <div style={{ ...styles.messageBubble, ...styles.botBubble }}>
                  <span style={styles.typingDot} />
                  <span style={{ ...styles.typingDot, animationDelay: '0.15s' }} />
                  <span style={{ ...styles.typingDot, animationDelay: '0.3s' }} />
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Zone de saisie */}
        <div style={styles.inputContainer}>
          <div style={styles.inputWrapper}>
            <textarea
              style={styles.textarea}
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyDown}
              placeholder="Posez votre question RH (congés, télétravail, paie...)"
            />
          </div>
          <button style={styles.button} onClick={sendMessage} disabled={loading || !input.trim()}>
            Envoyer
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  wrapper: {
    width: '100%',
    maxWidth: 420,
    height: 540,
    padding: 16,
    background: 'linear-gradient(135deg, #f5f7fb 0%, #e6edf7 50%, #f5f7fb 100%)',
    borderRadius: 24,
    boxShadow: '0 18px 45px rgba(15, 23, 42, 0.18)',
    boxSizing: 'border-box',
    display: 'flex',
    alignItems: 'stretch',
    justifyContent: 'center',
    fontFamily: '"Inter", system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
  },
  container: {
    flex: 1,
    backgroundColor: 'rgba(255,255,255,0.96)',
    borderRadius: 20,
    display: 'flex',
    flexDirection: 'column',
    overflow: 'hidden',
    border: '1px solid rgba(148, 163, 184, 0.35)',
    backdropFilter: 'blur(12px)',
  },
  header: {
    padding: '12px 16px',
    borderBottom: '1px solid rgba(226, 232, 240, 0.9)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    background:
      'radial-gradient(circle at top left, rgba(49, 130, 206, 0.12), transparent 55%), #ffffff',
  },
  headerLeft: {
    display: 'flex',
    alignItems: 'center',
    gap: 10,
  },
  avatar: {
    width: 36,
    height: 36,
    borderRadius: '50%',
    background:
      'linear-gradient(135deg, #2563eb 0%, #4f46e5 35%, #6366f1 70%, #0ea5e9 100%)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: '#f9fafb',
    fontWeight: 700,
    fontSize: 14,
    boxShadow: '0 8px 18px rgba(37, 99, 235, 0.35)',
  },
  avatarText: {
    letterSpacing: '0.03em',
  },
  title: {
    fontSize: 15,
    fontWeight: 600,
    color: '#0f172a',
  },
  subtitle: {
    fontSize: 12,
    color: '#64748b',
    display: 'flex',
    alignItems: 'center',
    gap: 6,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: '50%',
    backgroundColor: '#22c55e',
    boxShadow: '0 0 0 4px rgba(34, 197, 94, 0.25)',
  },
  messagesContainer: {
    flex: 1,
    background:
      'radial-gradient(circle at top, rgba(59, 130, 246, 0.05), transparent 60%), #f9fafb',
    padding: '10px 12px',
  },
  messages: {
    height: '100%',
    overflowY: 'auto',
    display: 'flex',
    flexDirection: 'column',
    gap: 8,
    paddingRight: 4,
  },
  messageRow: {
    display: 'flex',
    alignItems: 'flex-end',
    gap: 6,
  },
  messageBubble: {
    maxWidth: '75%',
    padding: '8px 12px',
    borderRadius: 16,
    fontSize: 13,
    lineHeight: 1.45,
    wordBreak: 'break-word',
    boxShadow: '0 4px 10px rgba(15, 23, 42, 0.10)',
  },
  userBubble: {
    background:
      'linear-gradient(135deg, #2563eb 0%, #4f46e5 50%, #6366f1 100%)',
    color: '#f9fafb',
    borderBottomRightRadius: 4,
  },
  botBubble: {
    backgroundColor: '#ffffff',
    color: '#0f172a',
    borderBottomLeftRadius: 4,
    border: '1px solid rgba(226, 232, 240, 0.9)',
  },
  smallAvatar: {
    width: 24,
    height: 24,
    borderRadius: '50%',
    backgroundColor: '#e0f2fe',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 11,
    color: '#0f172a',
    flexShrink: 0,
  },
  smallAvatarText: {
    fontWeight: 600,
  },
  typingDot: {
    display: 'inline-block',
    width: 6,
    height: 6,
    borderRadius: '50%',
    backgroundColor: '#94a3b8',
    marginRight: 4,
    animation: 'typingAnimation 1s infinite ease-in-out',
  },
  inputContainer: {
    borderTop: '1px solid rgba(226, 232, 240, 0.9)',
    padding: '10px 12px',
    display: 'flex',
    gap: 8,
    alignItems: 'center',
    backgroundColor: '#ffffff',
  },
  inputWrapper: {
    flex: 1,
    borderRadius: 999,
    backgroundColor: '#f8fafc',
    border: '1px solid rgba(226, 232, 240, 0.9)',
    padding: '6px 12px',
    display: 'flex',
    alignItems: 'center',
  },
  textarea: {
    flex: 1,
    border: 'none',
    outline: 'none',
    resize: 'none',
    height: 36,
    maxHeight: 64,
    fontSize: 13,
    fontFamily: 'inherit',
    backgroundColor: 'transparent',
    color: '#0f172a',
  },
  button: {
    border: 'none',
    borderRadius: 999,
    padding: '8px 16px',
    fontSize: 13,
    fontWeight: 600,
    cursor: 'pointer',
    background:
      'linear-gradient(135deg, #2563eb 0%, #4f46e5 50%, #6366f1 100%)',
    color: '#f9fafb',
    boxShadow: '0 8px 16px rgba(37, 99, 235, 0.4)',
    transition: 'transform 0.08s ease, box-shadow 0.08s ease, opacity 0.12s ease',
  },
};

// Injection rapide de l’animation CSS pour les points de frappe
const styleSheet = document.styleSheets[0];
if (styleSheet && styleSheet.insertRule) {
  const keyframes = `
  @keyframes typingAnimation {
    0% { transform: translateY(0); opacity: 0.4; }
    50% { transform: translateY(-2px); opacity: 1; }
    100% { transform: translateY(0); opacity: 0.4; }
  }`;
  try {
    styleSheet.insertRule(keyframes, styleSheet.cssRules.length);
  } catch (e) {
    // ignore si déjà inséré
  }
}

export default Chatbot;