export const certificateArray = [
  {giftCertificateId: 1, name: 'Certificate1'},
  {giftCertificateId: 3, name: 'Certificate3'},
  {giftCertificateId: 8, name: 'Certificate8'},
];

export const currentPage = {
  number: 2,
  size: 10,
  totalElements: 50,
  totalPages: 5,
};

export const createCertificateState = (
  status,
  error,
  certificateList,
  page
) => ({
  status,
  error,
  certificateList,
  page,
});
