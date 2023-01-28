import React from 'react';
import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import {TableCell, TableRow} from '@mui/material';

const CertificateTableRow = ({
  certificate,
  handleModalOpen,
  handleDeleteModalOpen,
}) => {
  const {
    giftCertificateId: id,
    name,
    duration,
    price,
    createDate,
    lastUpdateDate,
  } = certificate;

  const openEditModal = () => {
    handleModalOpen(certificate);
  };

  const openDeleteModal = () => {
    handleDeleteModalOpen(certificate);
  };

  return (
    <TableRow hover key={id}>
      <TableCell>{id}</TableCell>
      <TableCell>{name}</TableCell>
      <TableCell>{duration}</TableCell>
      <TableCell>{price}</TableCell>
      <TableCell>{createDate}</TableCell>
      <TableCell>{lastUpdateDate}</TableCell>
      <TableCell align="center" sx={{whiteSpace: 'nowrap'}}>
        <ModeEditIcon color="action" onClick={openEditModal} sx={{mr: 2}} />
        <DeleteIcon color="action" onClick={openDeleteModal} />
      </TableCell>
    </TableRow>
  );
};

export default CertificateTableRow;
