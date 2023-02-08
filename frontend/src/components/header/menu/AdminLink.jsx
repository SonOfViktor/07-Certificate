import {
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import {useNavigate} from 'react-router-dom';

const AdminLink = ({onClose}) => {
  const navigate = useNavigate();

  const handleAdminLinkClick = () => {
    navigate('/admin/certificate');
    onClose();
  };

  return (
    <ListItem disablePadding>
      <ListItemButton onClick={handleAdminLinkClick}>
        <ListItemIcon sx={{color: 'var(--primary)'}}>
          <AdminPanelSettingsIcon />
        </ListItemIcon>
        <ListItemText
          primary="Admin"
          primaryTypographyProps={{
            fontSize: 16,
            fontWeight: 500,
          }}
        />
      </ListItemButton>
    </ListItem>
  );
};

export default AdminLink;
