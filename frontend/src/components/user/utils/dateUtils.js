export const calculateExpireDate = (timestamp, duration) => {
  return new Date(Date.parse(timestamp) + duration * 24 * 3600 * 1000);
};

export const calculateDaysLeft = (timestamp, duration) => {
  const expireDate = calculateExpireDate(timestamp, duration);
  const daysLeft = Math.floor((+expireDate - Date.now()) / 24 / 3600 / 1000);

  return daysLeft < 0 ? 'Expired' : `${daysLeft} day${daysLeft !== 1 && 's'}`;
};
