import React, { useState, useEffect } from 'react';

const CalendrierEntretiens = ({ 
  rhId, 
  onDateSelect,        // Pour DashboardRH (sélection date seulement)
  onDateTimeSelect,    // Pour FormulaireEntretien (sélection date + heure)
  selectedDate,        // Date sélectionnée (pour DashboardRH)
  selectedDateTime,    // Date + heure sélectionnée (pour FormulaireEntretien)
  entretiens = []      // Entretiens passés en prop (optionnel)
}) => {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [localEntretiens, setLocalEntretiens] = useState([]);
  const [loading, setLoading] = useState(false);
  const [localSelectedDate, setLocalSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState('');

  // Déterminer le mode d'utilisation
  const isDateTimeMode = !!onDateTimeSelect;
  const isDateOnlyMode = !!onDateSelect;

  // Récupérer les entretiens futurs du RH (seulement si rhId est fourni et pas d'entretiens en prop)
  const fetchEntretiens = async () => {
    if (!rhId || entretiens.length > 0) return;
    
    setLoading(true);
    try {
      const response = await fetch(`http://localhost:8080/api/entretiens/rh/${rhId}/futurs`, {
        credentials: 'include'
      });
      if (response.ok) {
        const data = await response.json();
        setLocalEntretiens(data.entretiens || []);
      }
    } catch (error) {
      console.error('Erreur lors du chargement des entretiens:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEntretiens();
  }, [rhId]);

  // Utiliser les entretiens passés en prop ou ceux chargés localement
  const displayedEntretiens = entretiens.length > 0 ? entretiens : localEntretiens;

  // Obtenir les entretiens pour une date spécifique
  const getEntretiensForDate = (date) => {
    return displayedEntretiens.filter(entretien => {
      const entretienDate = new Date(entretien.dateHeureDebut);
      return (
        entretienDate.getDate() === date.getDate() &&
        entretienDate.getMonth() === date.getMonth() &&
        entretienDate.getFullYear() === date.getFullYear()
      );
    });
  };

  // Générer les crénaux horaires disponibles (seulement en mode date+time)
  const generateTimeSlots = (date) => {
    if (!isDateTimeMode) return [];
    
    const slots = [];
    const heuresOccupation = getOccupiedSlots(date);
    
    for (let hour = 8; hour <= 18; hour++) {
      for (let minute = 0; minute < 60; minute += 30) {
        const timeString = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
        const slotDateTime = new Date(date);
        slotDateTime.setHours(hour, minute, 0, 0);
        
        // Vérifier si le créneau n'est pas occupé
        const isOccupied = heuresOccupation.some(occupied => 
          slotDateTime.getTime() >= occupied.debut && 
          slotDateTime.getTime() < occupied.fin
        );

        if (!isOccupied && slotDateTime > new Date()) {
          slots.push({
            time: timeString,
            datetime: slotDateTime,
            available: true
          });
        }
      }
    }
    return slots;
  };

  // Obtenir les créneaux occupés pour une date
  const getOccupiedSlots = (date) => {
    const dateEntretiens = getEntretiensForDate(date);
    return dateEntretiens.map(entretien => ({
      debut: new Date(entretien.dateHeureDebut).getTime(),
      fin: new Date(entretien.dateHeureFin).getTime()
    }));
  };

  // Générer le calendrier pour le mois en cours
  const generateCalendar = () => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDate = new Date(firstDay);
    startDate.setDate(startDate.getDate() - firstDay.getDay());
    
    const endDate = new Date(lastDay);
    endDate.setDate(endDate.getDate() + (6 - lastDay.getDay()));
    
    const calendar = [];
    const current = new Date(startDate);
    
    while (current <= endDate) {
      calendar.push(new Date(current));
      current.setDate(current.getDate() + 1);
    }
    
    return calendar;
  };

  const calendarDays = generateCalendar();
  const timeSlots = localSelectedDate ? generateTimeSlots(localSelectedDate) : [];

  // Navigation entre les mois
  const previousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  // Vérifier si c'est aujourd'hui
  const isToday = (date) => {
    const today = new Date();
    return (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    );
  };

  // Vérifier si c'est le mois en cours
  const isCurrentMonth = (date) => {
    return date.getMonth() === currentDate.getMonth();
  };

  const handleDateClick = (date) => {
    setLocalSelectedDate(date);
    setSelectedTime('');

    if (isDateOnlyMode) {
      // Mode DashboardRH - seulement la date
      onDateSelect(date);
    } else if (isDateTimeMode) {
      // Mode FormulaireEntretien - réinitialiser la sélection temps
      onDateTimeSelect(null);
    }
  };

  const handleTimeClick = (timeSlot) => {
    if (!isDateTimeMode) return;
    
    setSelectedTime(timeSlot.time);
    onDateTimeSelect(timeSlot.datetime);
  };

  // Synchroniser la date sélectionnée depuis les props
  useEffect(() => {
    if (selectedDate) {
      setLocalSelectedDate(selectedDate);
    }
  }, [selectedDate]);

  // Synchroniser la date+heure sélectionnée depuis les props
  useEffect(() => {
    if (selectedDateTime) {
      setLocalSelectedDate(new Date(selectedDateTime));
      setSelectedTime(selectedDateTime.toLocaleTimeString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit'
      }));
    }
  }, [selectedDateTime]);

  return (
    <div className="space-y-6">
      {/* Calendrier */}
      <div className="bg-white rounded-xl border border-gray-200 p-4">
        <div className="flex justify-between items-center mb-4">
          <button 
            onClick={previousMonth}
            className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition-all duration-200 text-gray-600 hover:text-gray-800"
          >
            ‹
          </button>
          
          <h3 className="text-md font-semibold text-gray-800 capitalize">
            {currentDate.toLocaleDateString('fr-FR', { 
              month: 'long', 
              year: 'numeric' 
            })}
          </h3>
          
          <button 
            onClick={nextMonth}
            className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 transition-all duration-200 text-gray-600 hover:text-gray-800"
          >
            ›
          </button>
        </div>

        {/* Jours de la semaine */}
        <div className="grid grid-cols-7 gap-1 mb-2">
          {['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'].map(day => (
            <div key={day} className="text-center text-xs font-medium text-gray-500 py-1">
              {day}
            </div>
          ))}
        </div>

        {/* Grille du calendrier */}
        <div className="grid grid-cols-7 gap-1">
          {calendarDays.map((date, index) => {
            const dateEntretiens = getEntretiensForDate(date);
            const hasEntretiens = dateEntretiens.length > 0;
            const isSelected = localSelectedDate && 
              date.getDate() === localSelectedDate.getDate() &&
              date.getMonth() === localSelectedDate.getMonth() &&
              date.getFullYear() === localSelectedDate.getFullYear();

            return (
              <div
                key={index}
                className={`
                  relative h-10 flex items-center justify-center rounded-lg cursor-pointer transition-all duration-200 text-sm
                  ${isCurrentMonth(date) ? 'text-gray-800' : 'text-gray-400'}
                  ${isToday(date) ? 'bg-blue-100 border border-blue-300' : ''}
                  ${isSelected ? 'bg-blue-500 text-white shadow-md' : 'hover:bg-gray-100'}
                  ${hasEntretiens ? 'bg-gradient-to-br from-white to-blue-50 border border-blue-200' : ''}
                `}
                onClick={() => handleDateClick(date)}
              >
                <span className={`font-medium ${isSelected ? 'text-white' : ''}`}>
                  {date.getDate()}
                </span>
                
                {/* Point indicateur d'entretien */}
                {hasEntretiens && (
                  <div className={`
                    absolute bottom-1 w-1 h-1 rounded-full
                    ${isSelected ? 'bg-white' : 'bg-blue-500'}
                  `}></div>
                )}
              </div>
            );
          })}
        </div>
      </div>

      {/* Sélection de l'heure (seulement en mode date+time) */}
      {isDateTimeMode && localSelectedDate && (
        <div className="bg-gray-50 rounded-xl p-4 border border-gray-200">
          <h4 className="font-semibold text-gray-800 mb-3">
            Créneaux disponibles le {localSelectedDate.toLocaleDateString('fr-FR')}
          </h4>
          <div className="grid grid-cols-3 gap-2 max-h-60 overflow-y-auto">
            {timeSlots.length > 0 ? (
              timeSlots.map((slot, index) => (
                <button
                  key={index}
                  onClick={() => handleTimeClick(slot)}
                  className={`
                    py-2 px-3 rounded-lg text-sm font-medium transition-all duration-200
                    ${selectedTime === slot.time 
                      ? 'bg-blue-500 text-white shadow-md' 
                      : 'bg-white text-gray-700 hover:bg-blue-50 border border-gray-200'
                    }
                  `}
                >
                  {slot.time}
                </button>
              ))
            ) : (
              <div className="col-span-3 text-center py-4 text-gray-500">
                Aucun créneau disponible pour cette date
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default CalendrierEntretiens;